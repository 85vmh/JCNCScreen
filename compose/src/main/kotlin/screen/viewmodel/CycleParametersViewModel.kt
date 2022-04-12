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
            it.xEnd.value = calculateMinorDiameter(it.majorDiameter.value, it.threadPitch.value)
        }
    }

    private fun calculateMinorDiameter(majorDiameter: Double, threadPitch: Double): Double {
        val triangleHeight = sqrt(3.0) / 2 * threadPitch
        return majorDiameter - 2 * 5 / 8 * triangleHeight
    }

    fun teachInZEnd() {
        scope.launch {
            cycleParametersState.zEnd.value = useCase.getCurrentPoint().z
        }
    }
}