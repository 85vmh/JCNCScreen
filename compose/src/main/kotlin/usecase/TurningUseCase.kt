package usecase

import androidx.compose.runtime.mutableStateOf
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.IniFileRepository
import com.mindovercnc.base.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import screen.composables.FeedState
import screen.composables.SpindleState
import usecase.model.FeedRateMode
import usecase.model.SpindleControlMode
import kotlin.math.abs
import kotlin.math.tan

class TurningUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val halRepository: HalRepository,
    private val iniFileRepository: IniFileRepository
) {

    enum class FeedAxis {
        X, Z
    }

    enum class FeedDirection {
        NEGATIVE, POSITIVE
    }

    private data class Point(val x: Double, val z: Double)

    private var isFeeding = false
    private var isTaperTurning = false
    private var taperAngle = 45.0
    //val taper = mutableStateOf(false)

    init {
        val spindleIsOn = statusRepository.cncStatusFlow()
            .map { it.motionStatus.spindlesStatus[0].direction == SpindleStatus.Direction.REVERSE } //do this based on tool direction
            .distinctUntilChanged()

        combine(halRepository.getJoystickStatus(), spindleIsOn) { joystickStatus, spindleOn ->
            when (joystickStatus.position) {
                JoystickStatus.Position.Z_MINUS -> startTurning(FeedAxis.Z, FeedDirection.NEGATIVE)
                JoystickStatus.Position.Z_PLUS -> startTurning(FeedAxis.Z, FeedDirection.POSITIVE)
                JoystickStatus.Position.X_MINUS -> startTurning(FeedAxis.X, FeedDirection.NEGATIVE)
                JoystickStatus.Position.X_PLUS -> startTurning(FeedAxis.X, FeedDirection.POSITIVE)
                JoystickStatus.Position.NEUTRAL -> stopFeed()
            }
        }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }

    fun toggleTaperTurning(){
        isTaperTurning = !isTaperTurning
    }

    private suspend fun startTurning(axis: FeedAxis, feedDirection: FeedDirection) {
        if (isTaperTurning) {
            startTaperTurning(axis, feedDirection, taperAngle)
        } else {
            startStraightTurning(axis, feedDirection)
        }
        halRepository.setPowerFeedingStatus(true)
    }

    private fun startStraightTurning(axis: FeedAxis, feedDirection: FeedDirection) {
        val xAxisParam = iniFileRepository.getIniFile().joints[0]
        val zAxisParam = iniFileRepository.getIniFile().joints[1]

        val limit = when (axis) {
            FeedAxis.X -> when (feedDirection) {
                FeedDirection.NEGATIVE -> xAxisParam.minLimit
                FeedDirection.POSITIVE -> xAxisParam.maxLimit
            }
            FeedAxis.Z -> when (feedDirection) {
                FeedDirection.NEGATIVE -> zAxisParam.minLimit
                FeedDirection.POSITIVE -> zAxisParam.maxLimit
            }
        }
        isFeeding = true
        executeMdi("G53 G1 ${axis.name + limit}")
    }

    private suspend fun startTaperTurning(axis: FeedAxis, feedDirection: FeedDirection, angle: Double) {
        val xAxisParam = iniFileRepository.getIniFile().joints[0]
        val zAxisParam = iniFileRepository.getIniFile().joints[1]

        val cornerPoint = when (axis) {
            FeedAxis.X -> when (feedDirection) {
                FeedDirection.POSITIVE -> Point(xAxisParam.maxLimit, zAxisParam.minLimit)
                FeedDirection.NEGATIVE -> Point(xAxisParam.minLimit, zAxisParam.maxLimit)
            }
            FeedAxis.Z -> when (feedDirection) {
                FeedDirection.POSITIVE -> Point(xAxisParam.maxLimit, zAxisParam.maxLimit)
                FeedDirection.NEGATIVE -> Point(xAxisParam.minLimit, zAxisParam.minLimit)
            }
        }

        val it = statusRepository.cncStatusFlow().map { it.getG53Position() }.first()
        val startPoint = Point(it.x, it.z)
        println("start point: $startPoint")
        val destPoint = computeDestinationPoint(startPoint, cornerPoint, angle)
        println("dest point: $destPoint")
        isFeeding = true
        executeMdi("G53 G1 X${destPoint.x} Z${destPoint.z}")
    }

    private fun computeDestinationPoint(
        startPoint: Point,
        cornerPoint: Point,
        angle: Double
    ): Point {
        val opposite = abs(cornerPoint.x - startPoint.x)
        val adjacent = opposite / tan(Math.toRadians(angle))
        val maxDistZ = abs(cornerPoint.z - startPoint.z)
        return if (adjacent > maxDistZ) {
            val extraDistZ = adjacent - maxDistZ
            val sign = if (cornerPoint.x > 0) -1 else 1
            val smallOpposite = extraDistZ * tan(Math.toRadians(angle))
            val destPointX = cornerPoint.x + (smallOpposite * sign) //minus when xMaxLimit, plus when xMinLimit
            Point(destPointX, cornerPoint.z)
        } else {
            val sign = if (cornerPoint.z > 0) 1 else -1
            Point(cornerPoint.x, startPoint.z + (adjacent * sign))
        }
    }

    fun startFeed(){
        halRepository.setPowerFeedingStatus(true)
    }

    fun stopFeed() {
        halRepository.setPowerFeedingStatus(false)
        //if (isFeeding) {
//            commandRepository.taskAbort()
//            commandRepository.setTaskMode(TaskMode.TaskModeManual)
//            isFeeding = false
        //}
    }

    fun executeMdi(command: String) {
        commandRepository.setTaskMode(TaskMode.TaskModeMDI)
        commandRepository.executeMdiCommand(command)
    }

    fun getSpindleState(): Flow<SpindleState> {
        return combine(spindleMode, setSpindleSpeed) { mode, speed ->
            SpindleState(mode, speed ?: 0.0, speed ?: 0.0)
        }
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