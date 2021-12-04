package usecase

import codegen.ManualTurningHelper
import codegen.ManualTurningHelper.Axis
import codegen.ManualTurningHelper.Direction
import codegen.Point
import com.mindovercnc.base.*
import com.mindovercnc.base.data.*
import extensions.stripZeros
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import usecase.model.*

class ManualTurningUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val messagesRepository: MessagesRepository,
    private val halRepository: HalRepository,
    private val manualTurningHelper: ManualTurningHelper,
    private val settingsRepository: SettingsRepository,
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

    private val spindleOpAllowed = statusRepository.cncStatusFlow()
        .map { it.isHomed() }
        .distinctUntilChanged()

    init {
        val spindleIsOn = statusRepository.cncStatusFlow()
            .map { it.isSpindleOn } //do this based on tool direction
            .distinctUntilChanged()

        combine(
            halRepository.getJoystickStatus().onEach { println("---Joystick: $it") },
            spindleIsOn.onEach { println("---Spindle: $it") }
        ) { joystickStatus, spindleOn ->
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

        combine(
            halRepository.getSpindleSwitchStatus()
                .onEach {
                    println("---spindle switch is: $it")
                },
            spindleOpAllowed
        )
        { switchStatus, spindleAllowed ->
            when {
                spindleAllowed -> sendSpindleCommand(switchStatus)
                spindleAllowed.not() -> {
                    when (switchStatus) {
                        SpindleSwitchStatus.NEUTRAL -> messagesRepository.popMessage(UiMessageType.SpindleOperationNotAllowed)
                        else -> messagesRepository.pushMessage(UiMessageType.SpindleOperationNotAllowed)
                    }
                }
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

    private fun sendSpindleCommand(status: SpindleSwitchStatus) {
        when (status) {
            SpindleSwitchStatus.REV -> "M4"
            SpindleSwitchStatus.FWD -> "M3"
            SpindleSwitchStatus.NEUTRAL -> {
                halRepository.setSpindleStarted(false)
                null //for now spindle stop is done in HAL
            }
        }?.let {
            commandRepository.setTaskMode(TaskMode.TaskModeMDI)
            commandRepository.executeMdiCommand(it)
            halRepository.setSpindleStarted(true)
            commandRepository.setTaskMode(TaskMode.TaskModeManual)
        }
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
                    .map { Point(it.x * 2, it.z) } // *2 due to diameter mode
                    .first()
                manualTurningHelper.getTaperTurningCommand(axis, direction, startPoint, taperAngle.value)
            }
            else -> manualTurningHelper.getStraightTurningCommand(axis, direction)
        }
        val feedRate = setFeedRate.firstOrNull() ?: 0.0
        joystickFunction = JoystickFunction.Feeding
        executeMdi(command.plus(" F${feedRate.stripZeros()}"))
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

    private fun executeMdi(command: String) {
        commandRepository.setTaskMode(TaskMode.TaskModeMDI)
        println("---Execute MDI: $command")
        commandRepository.executeMdiCommand(command)
//        //When its MDI, do this
//        statusRepository.cncStatusFlow()
//            .onEach {
//                println("---*isMdi: ${it.isInMdiMode}")
//                println("---*isIdle: ${it.isInterpreterIdle}")
//                println("-------------------------")
//            }
//            .map { it.isInMdiMode && it.isInterpreterIdle }
//            .filter { it }
//            .distinctUntilChanged() //this is to make sure that it gets executed only once??
//            .onEach {
//                println("---Execute MDI: $command")
//                commandRepository.executeMdiCommand(command)
//            }
//            .launchIn(scope)
    }

    suspend fun getSpindleState(): SpindleState {
        val mode = spindleMode.first()
        var rpmSpeed = settingsRepository.get(IntegerKey.RpmValue, 300)
        var cssSpeed = settingsRepository.get(IntegerKey.CssValue, 230)
        val setSpindleSpeed = setSpindleSpeed.first()

        when (mode) {
            SpindleControlMode.RPM -> setSpindleSpeed?.let { rpmSpeed = it }
            SpindleControlMode.CSS -> setSpindleSpeed?.let { cssSpeed = it }
        }

        val maxSpeed = cssMaxSpeed.first()
        val orientedStop = settingsRepository.get(BooleanKey.OrientedStop)
        val defaultAngle = settingsRepository.get(DoubleKey.OrientAngle)
        return SpindleState(
            defaultControlMode = mode,
            defaultRpmValue = rpmSpeed,
            defaultCssValue = cssSpeed,
            defaultMaxCssRpm = if (maxSpeed > 0) maxSpeed else settingsRepository.get(IntegerKey.MaxCssRpm, 1500),
            orientedStop = orientedStop,
            defaultStopAngle = defaultAngle
        )
    }

    suspend fun getFeedState(): FeedState {
        val mode = feedMode
            .map {
                when (it) {
                    FeedMode.UNITS_PER_REVOLUTION -> FeedRateMode.UNITS_PER_REVOLUTION
                    FeedMode.UNITS_PER_MINUTE -> FeedRateMode.UNITS_PER_MINUTE
                    else -> FeedRateMode.UNITS_PER_REVOLUTION
                }
            }
            .first()
        var feedPerRev = settingsRepository.get(DoubleKey.FeedPerRev, 0.1)
        var feedPerMin = settingsRepository.get(DoubleKey.FeedPerMin, 50.0)
        when (mode) {
            FeedRateMode.UNITS_PER_REVOLUTION -> setFeedRate.first()?.let { feedPerRev = it }
            FeedRateMode.UNITS_PER_MINUTE -> setFeedRate.first()?.let { feedPerMin = it }
        }

        return FeedState(mode, feedPerRev, feedPerMin)
    }

    fun applySpindleSettings(spindleState: SpindleState) {
        val rpmValue = spindleState.rpmValue.value.toInt()
        val cssValue = spindleState.cssValue.value.toInt()
        val maxSpeed = spindleState.maxCssRpm.value.toInt()
        when (spindleState.spindleMode.value) {
            SpindleControlMode.RPM -> executeMdi("G97 S$rpmValue")
            SpindleControlMode.CSS -> executeMdi("G96 D$maxSpeed S$cssValue")
        }
        settingsRepository.put(IntegerKey.RpmValue, rpmValue)
        settingsRepository.put(IntegerKey.CssValue, cssValue)
        settingsRepository.put(IntegerKey.MaxCssRpm, maxSpeed)
        settingsRepository.put(BooleanKey.OrientedStop, spindleState.orientedStop.value)
        settingsRepository.put(DoubleKey.OrientAngle, spindleState.stopAngle.value.toDouble())
    }

    fun applyFeedSettings(feedState: FeedState) {
        val feedPerRev = feedState.unitsPerRevValue.value.toDouble()
        val feedPerMin = feedState.unitsPerMinValue.value.toDouble()
        when (feedState.feedRateMode.value) {
            FeedRateMode.UNITS_PER_REVOLUTION -> executeMdi("G95 F$feedPerRev")
            FeedRateMode.UNITS_PER_MINUTE -> executeMdi("G94 F$feedPerMin")
        }
        settingsRepository.put(DoubleKey.FeedPerRev, feedPerRev)
        settingsRepository.put(DoubleKey.FeedPerMin, feedPerMin)
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
        .map { it.taskStatus.setSpindleSpeed?.toInt() }
        .distinctUntilChanged()

    val spindleOverride = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].spindleScale }
        .distinctUntilChanged()

    val actualSpindleSpeed = halRepository.actualSpindleSpeed().distinctUntilChanged()

    private val cssMaxSpeed = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].cssMaximum.toInt() }
        .distinctUntilChanged()

    val handwheelsState = combine(
        statusRepository.cncStatusFlow().map { it.isInManualMode },
        halRepository.jogIncrementValue()
    ) { isManualMode, jogIncrement ->
        HandwheelsState(isManualMode, jogIncrement)
    }
}