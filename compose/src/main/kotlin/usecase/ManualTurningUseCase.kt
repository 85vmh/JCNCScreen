package usecase

import codegen.ManualTurningHelper
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import usecase.model.FeedRateMode
import usecase.model.FeedState
import usecase.model.SpindleControlMode
import usecase.model.SpindleState
import codegen.ManualTurningHelper.Axis
import codegen.ManualTurningHelper.Direction
import codegen.Point
import com.mindovercnc.base.MessagesRepository
import extensions.stripZeros

class ManualTurningUseCase(
    scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val messagesRepository: MessagesRepository,
    private val halRepository: HalRepository,
    private val manualTurningHelper: ManualTurningHelper
) {

    private var joystickFunction = JoystickFunction.None
    private var joggedAxis: Axis? = null
    private var joystickResetRequired = false

    private val isTaperTurning = MutableStateFlow(false)

    private val taperAngle = MutableStateFlow(45.0)

    enum class JoystickFunction {
        None, Feeding, Jogging
    }

    val taperTurningActive = isTaperTurning.asStateFlow()

    init {
        val spindleIsOn = statusRepository.cncStatusFlow()
            .map { it.isSpindleOn } //do this based on tool direction
            .distinctUntilChanged()

        combine(
            halRepository.getJoystickStatus().onEach { println("---Joystick: $it") },
            spindleIsOn.onEach { println("---Spindle: $it") }
        ) { joystickStatus, spindleOn ->
            println("Status: $joystickStatus, Spindle: $spindleOn")
            when (joystickStatus.position) {
                JoystickStatus.Position.ZMinus -> handleJoystick(Axis.Z, Direction.Negative, joystickStatus.isRapid, spindleOn)
                JoystickStatus.Position.ZPlus -> handleJoystick(Axis.Z, Direction.Positive, joystickStatus.isRapid, spindleOn)
                JoystickStatus.Position.XMinus -> handleJoystick(Axis.X, Direction.Negative, joystickStatus.isRapid, spindleOn)
                JoystickStatus.Position.XPlus -> handleJoystick(Axis.X, Direction.Positive, joystickStatus.isRapid, spindleOn)
                JoystickStatus.Position.Neutral -> handleBackToNeutral()
            }
        }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)

        halRepository.getCycleStopStatus()
            .filter { it }
            .onEach {
                isTaperTurning.value = false
                stopFeeding()
            }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }

    fun toggleTaperTurning() {
        isTaperTurning.value = !isTaperTurning.value
    }


    private suspend fun handleJoystick(axis: Axis, direction: Direction, isRapid: Boolean, isSpindleOn: Boolean) {
        if (isRapid) {
            if (joystickFunction == JoystickFunction.Feeding) {
                println("---Rapid requested, stop feeding")
                stopFeeding()
            }
            startJogging(axis, direction)
            joystickResetRequired = true
        } else {
            if (joystickFunction == JoystickFunction.Jogging) {
                stopJogging(axis)
                joystickResetRequired = true
            }
        }

        if (isSpindleOn) {
            if (joystickResetRequired) {
                println("---Joystick is not in neutral state")
                messagesRepository.pushMessage(UiMessageType.JoystickResetRequired)
            } else {
                startFeeding(axis, direction)
            }
        } else {
            when (joystickFunction) {
                JoystickFunction.Feeding -> {
                    println("---Spindle was stopped while feeding, stopFeeding!")
                    stopFeeding()
                    joystickResetRequired = true
                    println("---Set resetRequired flag")
                }
                JoystickFunction.Jogging -> {
                    //nothing to do here
                }
                JoystickFunction.None -> {
                    println("---Feed attempted while spindle is off")
                    messagesRepository.pushMessage(UiMessageType.JoystickCannotFeedWithSpindleOff)
                }
            }
        }
    }

    private fun handleBackToNeutral() {
        when (joystickFunction) {
            JoystickFunction.Feeding -> stopFeeding()
            JoystickFunction.Jogging -> joggedAxis?.let { stopJogging(it) }
            JoystickFunction.None -> {
                joggedAxis = null
                messagesRepository.popMessage(UiMessageType.JoystickCannotFeedWithSpindleOff)
            }
        }
        if (joystickResetRequired) {
            joystickResetRequired = false
            messagesRepository.popMessage(UiMessageType.JoystickResetRequired)
        }
    }

    private suspend fun startFeeding(axis: Axis, direction: Direction) {
        val command = when {
            isTaperTurning.value -> {
                val startPoint = statusRepository.cncStatusFlow()
                    .map { it.g53Position }
                    .map { Point(it.x, it.z) }
                    .first()
                manualTurningHelper.getTaperTurningCommand(axis, direction, startPoint, taperAngle.value)
            }
            else -> manualTurningHelper.getStraightTurningCommand(axis, direction)
        }
        val feedRate = setFeedRate.firstOrNull() ?: 0.0
        joystickFunction = JoystickFunction.Feeding
        val cmdWithFeed = command.plus(" F${feedRate.stripZeros()}")
        println("---Command to execute: $cmdWithFeed")
        executeMdi(cmdWithFeed)
        halRepository.setPowerFeedingStatus(true)
    }

    private fun stopFeeding() {
        if (joystickFunction == JoystickFunction.Feeding) {
            halRepository.setPowerFeedingStatus(false)
            joystickFunction = JoystickFunction.None
            println("---Stop feeding")
        }
    }

    private suspend fun startJogging(axis: Axis, feedDirection: Direction) {
        commandRepository.setTaskMode(TaskMode.TaskModeManual)
        val jogVelocity = statusRepository.cncStatusFlow().map { it.jogVelocity }.first()
        val jogDirection = when (feedDirection) {
            Direction.Positive -> jogVelocity
            Direction.Negative -> jogVelocity * -1
        }
        println("---Jog $axis with velocity: $jogDirection")
        commandRepository.jogContinuous(JogMode.AXIS, axis.index, jogDirection)
        joggedAxis = axis
        joystickFunction = JoystickFunction.Jogging
    }

    private fun stopJogging(axis: Axis) {
        if (joystickFunction == JoystickFunction.Jogging) {
            commandRepository.setTaskMode(TaskMode.TaskModeManual)
            commandRepository.jogStop(JogMode.AXIS, axis.index)
            joystickFunction = JoystickFunction.None
            println("---Stop jogging")
        }
    }

    fun executeMdi(command: String) {
        commandRepository.setTaskMode(TaskMode.TaskModeMDI)
        commandRepository.executeMdiCommand(command)
    }

    suspend fun getSpindleState(): SpindleState {
        val mode = spindleMode.first()
        val speed = setSpindleSpeed.first()
        return SpindleState(mode, speed ?: 0.0, speed ?: 0.0)
    }

    fun getFeedState(): Flow<FeedState> {
        return combine(feedMode, setFeedRate) { mode, feed ->
            when (mode) {
                FeedMode.UNITS_PER_REVOLUTION -> FeedState(FeedRateMode.UNITS_PER_REVOLUTION, feed ?: 0.0, feed ?: 0.0)
                FeedMode.UNITS_PER_MINUTE -> FeedState(FeedRateMode.UNITS_PER_MINUTE, feed ?: 0.0, feed ?: 0.0)
                else -> FeedState(FeedRateMode.UNITS_PER_REVOLUTION, feed ?: 0.0, feed ?: 0.0)
            }
        }
    }

    val spindleMode = statusRepository.cncStatusFlow()
        .map {
            when (it.taskStatus.activeCodes.spindleMode) {
                SpindleMode.RPM -> SpindleControlMode.RPM
                SpindleMode.CSS -> SpindleControlMode.CSS
                else -> SpindleControlMode.RPM
            }
        }
        .distinctUntilChanged()

    val feedMode = statusRepository.cncStatusFlow()
        .map { it.taskStatus.activeCodes.feedMode }
        .distinctUntilChanged()

    val distanceMode = statusRepository.cncStatusFlow()
        .map { it.taskStatus.activeCodes.distanceMode }
        .distinctUntilChanged()

    val setFeedRate = statusRepository.cncStatusFlow()
        .map { it.taskStatus.setFeedRate }
        .distinctUntilChanged()

    val feedOverride = statusRepository.cncStatusFlow()
        .map { it.motionStatus.trajectoryStatus.velocityScale }
        .distinctUntilChanged()

    val actualFeedRate = combine(setFeedRate, feedOverride) { feed, scale -> (feed ?: 0.0) * scale / 100 }
        .distinctUntilChanged()

    val setSpindleSpeed = statusRepository.cncStatusFlow()
        .map { it.taskStatus.setSpindleSpeed }
        .distinctUntilChanged()

    val spindleOverride = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].spindleScale }
        .distinctUntilChanged()

    val actualSpindleSpeed = halRepository.actualSpindleSpeed().distinctUntilChanged()

    val cssMaxSpeed = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].cssMaximum }
        .distinctUntilChanged()
}