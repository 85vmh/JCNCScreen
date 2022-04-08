package usecase

import codegen.Point
import codegen.ThreadingOperation
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.SettingsRepository
import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.getDisplayablePosition
import extensions.stripZeros
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import screen.uimodel.SimpleCycle
import usecase.model.*
import kotlin.math.sqrt

class SimpleCyclesUseCase(
    scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository,
) {

    var isInEditMode = false
    private val _isSimpleCycleActive = MutableStateFlow(false)
    private val _subroutineToCall = MutableStateFlow("")

    val isSimpleCycleActive = _isSimpleCycleActive.asStateFlow()
    val subroutineToCall = _subroutineToCall.asStateFlow()
    var currentCycle: SimpleCycle? = null

    init {
        halRepository.getCycleStopStatus()
            .filter { it }
            .filter { isSimpleCycleActive.value }
            .onEach {
                if (isCycleRunning()) {
                    commandRepository.taskAbort()
                    commandRepository.setTaskMode(TaskMode.TaskModeManual)
                } else {
                    _isSimpleCycleActive.value = false
                }
            }.launchIn(scope)

        halRepository.getCycleStartStatus()
            .filter { it }
            .filter { isSimpleCycleActive.value && subroutineToCall.value.isNotEmpty() }
            .onEach {
                commandRepository.setTaskMode(TaskMode.TaskModeMDI)
                commandRepository.executeMdiCommand(subroutineToCall.value)
            }.launchIn(scope)
    }

    fun getInitialParameters(simpleCycle: SimpleCycle): CycleParametersState {
        this.currentCycle = simpleCycle
        return when (simpleCycle) {
            SimpleCycle.Turning -> TurningParameterState(
                xEnd = 0.0,
                zEnd = 0.0,
                doc = 1.0
            )
            SimpleCycle.Boring -> BoringParameterState(
                xEnd = 0.0,
                zEnd = 0.0,
                doc = 1.0
            )
            SimpleCycle.Facing -> FacingParameterState(
                xEnd = 0.0,
                zEnd = 0.0,
                doc = 1.0
            )
            SimpleCycle.Threading -> ThreadingParameterState(
                xEnd = 0.0,
                zEnd = 0.0,
                firstPassDepth = 0.2,
                taper = ThreadingOperation.Taper.AtEnd(1.0),
                depthDegression = ThreadingOperation.DepthDegression.ConstantArea,
                compoundSlideAngle = ThreadingOperation.CompoundSlideAngle.Angle290,
                threadPitch = 1.0,
                springPasses = 1,
                majorDiameter = 0.0
            )
            else -> DummyParameterState()
        }
    }

    fun applyParameters(cycleParametersState: CycleParametersState) {
        val mdiCommand = when (cycleParametersState) {
            is TurningParameterState -> getTurningCommand(cycleParametersState)
            is BoringParameterState -> getBoringCommand(cycleParametersState)
            is FacingParameterState -> getFacingCommand(cycleParametersState)
            is ThreadingParameterState -> getThreadingCommand(cycleParametersState)
            else -> "Not implemented yet"
        }
        println("---SimpleCycle MDI command: $mdiCommand")
        _subroutineToCall.value = mdiCommand
        _isSimpleCycleActive.value = true
    }

    private fun getTurningCommand(parameters: TurningParameterState): String {
        val xEnd = parameters.xEnd.value.stripZeros()
        val zEnd = parameters.zEnd.value.stripZeros()
        val doc = parameters.doc.value.stripZeros()
        val turnAngle = parameters.turnAngle.value.stripZeros()
        val filletRadius = parameters.filletRadius.value.stripZeros()
        return "o<turning> call [$xEnd] [$zEnd] [$doc] [$turnAngle] [$filletRadius]"
    }

    private fun getBoringCommand(parameters: BoringParameterState): String {
        val xEnd = parameters.xEnd.value.stripZeros()
        val zEnd = parameters.zEnd.value.stripZeros()
        val doc = parameters.doc.value.stripZeros()
        val turnAngle = parameters.turnAngle.value.stripZeros()
        val filletRadius = parameters.filletRadius.value.stripZeros()
        return "o<boring> call [$xEnd] [$zEnd] [$doc] [$turnAngle] [$filletRadius]"
    }

    private fun getFacingCommand(parameters: FacingParameterState): String {
        val xEnd = parameters.xEnd.value.stripZeros()
        val zEnd = parameters.zEnd.value.stripZeros()
        val doc = parameters.doc.value.stripZeros()
        return "o<facing> call [$xEnd] [$zEnd] [$doc]"
    }

    private fun getThreadingCommand(parameters: ThreadingParameterState): String {
        val pitch = parameters.threadPitch.value.stripZeros()
        val zEnd = parameters.zEnd.value.stripZeros()
        val majorDiameter = parameters.majorDiameter.value.stripZeros()
        val firstPassDiameter = (parameters.majorDiameter.value - 2 * parameters.doc.value).stripZeros()
        //val minorDiameter = parameters.xEnd.value.stripZeros()
        val minorDiameter = getMinorDiameter(parameters.majorDiameter.value, parameters.threadPitch.value).stripZeros()
        val depthDegression = parameters.depthDegression.value.value
        val compoundAngle = parameters.compoundSlideAngle.value.value
        val taper = parameters.taper.value
        val springPasses = parameters.springPasses.value
        return "o<od_threading> call [$pitch] [$zEnd] [$majorDiameter] [$firstPassDiameter] [$minorDiameter] [$depthDegression] [$compoundAngle] [${taper.code}] [${taper.length}] [$springPasses]"
    }

    private fun getMinorDiameter(majorDiameter: Double, threadPitch: Double): Double {
        val triangleHeight = sqrt(3.0) / 2 * threadPitch
        return majorDiameter - (triangleHeight - (triangleHeight / 4) * 2)
    }

    suspend fun getCurrentPoint() = statusRepository.cncStatusFlow()
        .map { it.getDisplayablePosition() }
        .map { Point(it.x * 2, it.z) } // *2 due to diameter mode
        .first()

    private suspend fun isCycleRunning() = statusRepository.cncStatusFlow()
        .map { it.isInMdiMode }
        .first()
}