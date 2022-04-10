package screen.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import screen.uimodel.SimpleCycle
import usecase.SimpleCyclesUseCase
import usecase.model.ThreadingParameterState
import kotlin.math.sqrt

class CycleParametersViewModel(
    val scope: CoroutineScope,
    val simpleCycle: SimpleCycle,
    val useCase: SimpleCyclesUseCase
) {

    val cycleParametersState = useCase.getInitialParameters(simpleCycle)

    fun enterEditMode() {
        useCase.isInEditMode = true
    }

    fun exitEditMode() {
        useCase.isInEditMode = false
    }

    fun save() {
        useCase.applyParameters(cycleParametersState)
        useCase.isInEditMode = false
    }

    fun teachInXEnd() {
        scope.launch {
            cycleParametersState.xEnd.value = useCase.getCurrentPoint().x
        }
    }

    fun teachInMajorDiameter() {
        scope.launch {
            (cycleParametersState as? ThreadingParameterState)?.let {
                it.majorDiameter.value = useCase.getCurrentPoint().x
            }
        }
    }

    fun teachInMinorDiameter() {
        (cycleParametersState as? ThreadingParameterState)?.let {
            it.xEnd.value = calculateMinorDiameter(it.majorDiameter.value, it.threadPitch.value, true)
        }
    }

    private fun calculateMinorDiameter(majorDiameter: Double, threadPitch: Double, isExternalThread: Boolean): Double {
        val triangleHeight = sqrt(3.0) / 2 * threadPitch
        // threadPeak is negative for external threads, positive for internal threads.
        val amount = when {
            isExternalThread -> triangleHeight - (triangleHeight / 4)
            else -> triangleHeight - (triangleHeight / 8)
        }
        return majorDiameter - amount
    }

    fun teachInZEnd() {
        scope.launch {
            cycleParametersState.zEnd.value = useCase.getCurrentPoint().z
        }
    }
}