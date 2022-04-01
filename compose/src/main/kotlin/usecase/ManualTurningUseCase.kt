package usecase

import codegen.ManualTurningHelper
import codegen.Point
import com.mindovercnc.base.*
import com.mindovercnc.base.data.*
import extensions.stripZeros
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import usecase.model.*

class ManualTurningUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val messagesRepository: MessagesRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository,
    private val iniFileRepository: IniFileRepository
) {

    private var feedJob: Job? = null
    private var joystickFunction = JoystickFunction.None
    private var joggedAxis: Axis? = null
    private var joystickResetRequired = false

    private val isTaperTurning = MutableStateFlow(false)
    private val taperAngle get() = getTaperState().taperAngle.value

    enum class JoystickFunction {
        None, Feeding, Jogging
    }

    val taperTurningActive = isTaperTurning.asStateFlow()

    private val spindleOpAllowed = statusRepository.cncStatusFlow().map { it.isHomed() }.distinctUntilChanged()

    init {
        val spindleIsOn = statusRepository.cncStatusFlow().map { it.isSpindleOn } //do this based on tool direction
            .distinctUntilChanged()

        combine(
            halRepository.getJoystickStatus(),
            spindleIsOn
        ) { joystickStatus, spindleOn ->
            //println("---Spindle: $spindleOn")
            println("---Joystick: $joystickStatus")
            handleJoystick(joystickStatus, spindleOn)
        }.launchIn(scope)

        combine(
            statusRepository.cncStatusFlow().map { it.isInAutoMode }.distinctUntilChanged(),
            halRepository.getSpindleSwitchStatus().onEach {
                println("---Spindle switch is: $it")
            }, spindleOpAllowed
        ) { inAutoMode, switchStatus, spindleAllowed ->
            when {
                inAutoMode -> setHalSpindleStatus(switchStatus)
                spindleAllowed -> sendSpindleCommand(switchStatus)
                spindleAllowed.not() -> {
                    when (switchStatus) {
                        SpindleSwitchStatus.NEUTRAL -> messagesRepository.popMessage(UiMessage.SpindleOperationNotAllowed)
                        else -> messagesRepository.pushMessage(UiMessage.SpindleOperationNotAllowed)
                    }
                }
            }
        }.launchIn(scope)

        halRepository.getCycleStopStatus().filter { it }.onEach {
            isTaperTurning.value = false
            stopFeeding()
        }.launchIn(scope)
    }

    fun toggleTaperTurning() {
        isTaperTurning.value = isTaperTurning.value.not()
    }

    private fun sendSpindleCommand(status: SpindleSwitchStatus) {
        when (status) {
            SpindleSwitchStatus.REV -> "M4"
            SpindleSwitchStatus.FWD -> "M3"
            SpindleSwitchStatus.NEUTRAL -> {
                halRepository.setSpindleStarted(false)
                null //for now spindle stop is done in HAL and Classic Ladder
            }
        }?.let {
            val cmd = it + settingsRepository.getSpindleStartParameters()

//            if (!statusRepository.cncStatusFlow().first().isInMdiMode) {
//                commandRepository.setTaskMode(TaskMode.TaskModeMDI)
//            }
            commandRepository.setTaskMode(TaskMode.TaskModeMDI)
            commandRepository.executeMdiCommand(cmd)
            halRepository.setSpindleStarted(true)
            commandRepository.setTaskMode(TaskMode.TaskModeManual)
        }
    }

    private fun setHalSpindleStatus(status: SpindleSwitchStatus) {
        when (status) {
            SpindleSwitchStatus.REV,
            SpindleSwitchStatus.FWD -> halRepository.setSpindleStarted(true)
            SpindleSwitchStatus.NEUTRAL -> halRepository.setSpindleStarted(false)
        }
    }

    private suspend fun handleJoystick(joystickStatus: JoystickStatus, isSpindleOn: Boolean) {
        if (joystickStatus.position == JoystickStatus.Position.Neutral) {
            handleBackToNeutral()
            return
        }

        val axis = joystickStatus.position.axis!!
        val direction = joystickStatus.position.direction!!

        if (joystickStatus.isRapid) {
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
                messagesRepository.pushMessage(UiMessage.JoystickResetRequired)
            } else {
                delayedFeed(axis, direction)
            }
        } else {
            when (joystickFunction) {
                JoystickFunction.Feeding -> {
                    println("---Spindle was stopped while feeding, stopFeeding!")
                    stopFeeding()
                    joystickResetRequired = true
                }
                JoystickFunction.Jogging -> {
                    //nothing to do here
                }
                JoystickFunction.None -> {
                    println("---Feed attempted while spindle is off")
                    messagesRepository.pushMessage(UiMessage.JoystickCannotFeedWithSpindleOff)
                }
            }
        }
    }

    private suspend fun delayedFeed(axis: Axis, direction: Direction) {
        println("---Delayed feed")
        feedJob?.cancel()
        feedJob = coroutineScope {
            launch {
                delay(1000L)
                println("---Start feeding")
                startFeeding(axis, direction)
            }
        }
    }

    private fun handleBackToNeutral() {
        println("---handleBackToNeutral()")
        if (feedJob != null) {
            println("Cancel feed job: $feedJob")
            feedJob?.cancel()
        } else {
            println("No job to cancel")
        }

        when (joystickFunction) {
            JoystickFunction.Feeding -> stopFeeding()
            JoystickFunction.Jogging -> joggedAxis?.let { stopJogging(it) }
            JoystickFunction.None -> {
                joggedAxis = null
                messagesRepository.popMessage(UiMessage.JoystickCannotFeedWithSpindleOff)
            }
        }
        if (joystickResetRequired) {
            joystickResetRequired = false
            messagesRepository.popMessage(UiMessage.JoystickResetRequired)
        }
    }

    private suspend fun startFeeding(axis: Axis, direction: Direction) {
        val command = when {
            isTaperTurning.value -> {
                val startPoint = statusRepository.cncStatusFlow()
                    .map { it.g53Position }
                    .map { Point(it.x * 2, it.z) } // *2 due to diameter mode
                    .first()
                ManualTurningHelper.getTaperTurningCommand(axis, direction, iniFileRepository.getActiveLimits().inSafeRange(), startPoint, taperAngle)
            }
            else -> ManualTurningHelper.getStraightTurningCommand(axis, direction, iniFileRepository.getActiveLimits().inSafeRange())
        }
        val feedRate = setFeedRate.firstOrNull() ?: 0.0
        joystickFunction = JoystickFunction.Feeding
        executeMdi(command.plus(" F${feedRate.stripZeros()}"))
        halRepository.setPowerFeedingStatus(true)
    }

    private fun stopFeeding() {
        feedJob?.cancel()
        if (joystickFunction == JoystickFunction.Feeding) {
            halRepository.setPowerFeedingStatus(false)
            joystickFunction = JoystickFunction.None
            println("---Stop feeding")
        }
    }

    private suspend fun startJogging(axis: Axis, feedDirection: Direction) {
        stopFeeding()
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
        commandRepository.executeMdiCommand(command)
    }

    fun getTaperState() = TaperState(
        taperAngle = settingsRepository.get(DoubleKey.TaperAngle, 45.0)
    )

    fun getSpindleState() = SpindleState(
        defaultControlMode = SpindleControlMode.values()[settingsRepository.get(IntegerKey.SpindleMode, SpindleControlMode.RPM.ordinal)],
        defaultRpmValue = settingsRepository.get(IntegerKey.RpmValue, 300),
        defaultCssValue = settingsRepository.get(IntegerKey.CssValue, 230),
        defaultMaxCssRpm = settingsRepository.get(IntegerKey.MaxCssRpm, 1500),
        orientedStop = settingsRepository.get(BooleanKey.OrientedStop),
        defaultStopAngle = settingsRepository.get(DoubleKey.OrientAngle)
    )

    fun getFeedState() = FeedState(
        defaultFeedRateMode = FeedRateMode.values()[settingsRepository.get(IntegerKey.FeedMode, FeedRateMode.UNITS_PER_REVOLUTION.ordinal)],
        defaultUnitsPerRevValue = settingsRepository.get(DoubleKey.FeedPerRev, 0.1),
        defaultUnitsPerMinValue = settingsRepository.get(DoubleKey.FeedPerMin, 50.0)
    )

    fun applySpindleSettings(spindleState: SpindleState) {
        settingsRepository.apply {
            put(IntegerKey.SpindleMode, spindleState.spindleMode.value.ordinal)
            put(IntegerKey.RpmValue, spindleState.rpmValue.value)
            put(IntegerKey.CssValue, spindleState.cssValue.value)
            put(IntegerKey.MaxCssRpm, spindleState.maxCssRpm.value)
            put(BooleanKey.OrientedStop, spindleState.orientedStop.value)
            put(DoubleKey.OrientAngle, spindleState.stopAngle.value.toDouble())
        }
    }

    fun applyFeedSettings(feedState: FeedState) {
        settingsRepository.apply {
            put(IntegerKey.FeedMode, feedState.feedRateMode.value.ordinal)
            put(DoubleKey.FeedPerRev, feedState.unitsPerRevValue.value)
            put(DoubleKey.FeedPerMin, feedState.unitsPerMinValue.value)
        }
    }

    fun applyTaperSettings(taperState: TaperState) {
        settingsRepository.apply {
            put(DoubleKey.TaperAngle, taperState.taperAngle.value)
        }
    }

    private val setFeedRate = statusRepository.cncStatusFlow().map { it.taskStatus.setFeedRate }.distinctUntilChanged()

    val feedOverride = statusRepository.cncStatusFlow().map { it.motionStatus.trajectoryStatus.velocityScale }.distinctUntilChanged()

    //TODO: check if this is correct when feeding in mm/min
    val actualFeedRate = combine(setFeedRate, feedOverride) { feed, scale -> (feed ?: 0.0) * scale / 100 }.distinctUntilChanged()

    val spindleOverride = statusRepository.cncStatusFlow().map { it.motionStatus.spindlesStatus[0].spindleScale }.map { it.toInt() }.distinctUntilChanged()

    val actualSpindleSpeed = halRepository.actualSpindleSpeed().map { it.toInt() }.distinctUntilChanged()

    @OptIn(FlowPreview::class)
    val handwheelsState = combine(
        statusRepository.cncStatusFlow().map { it.isInManualMode },
        halRepository.jogIncrementValue().debounce(200L)
    ) { isManualMode, jogIncrement -> HandwheelsState(isManualMode, jogIncrement) }

    private fun SettingsRepository.getSpindleStartParameters(): String {
        val parameters = StringBuilder()
        val spindleMode = settingsRepository.get(IntegerKey.SpindleMode, SpindleControlMode.RPM.ordinal)
        val feedMode = settingsRepository.get(IntegerKey.FeedMode, FeedRateMode.UNITS_PER_REVOLUTION.ordinal)
        when (SpindleControlMode.values()[spindleMode]) {
            SpindleControlMode.RPM -> {
                val rpmSpeed = get(IntegerKey.RpmValue, 300)
                parameters.append(" G97 S$rpmSpeed")
            }
            SpindleControlMode.CSS -> {
                val cssValue = get(IntegerKey.CssValue, 230)
                val cssMaxRpm = get(IntegerKey.MaxCssRpm, 1500)
                parameters.append(" G96 D$cssMaxRpm S$cssValue")
            }
        }
        when (FeedRateMode.values()[feedMode]) {
            FeedRateMode.UNITS_PER_REVOLUTION -> {
                val feedPerRev = settingsRepository.get(DoubleKey.FeedPerRev, 0.1)
                parameters.append(" G95 F$feedPerRev")
            }
            else -> {
                val feedPerMin = settingsRepository.get(DoubleKey.FeedPerMin, 50.0)
                parameters.append(" G94 F$feedPerMin")
            }
        }

        return parameters.toString()
    }
}