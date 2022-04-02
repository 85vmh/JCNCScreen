package screen.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import screen.uimodel.SimpleCycle
import usecase.SimpleCyclesUseCase

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

    fun teachInX() {
        scope.launch {
            cycleParametersState.xEnd.value = useCase.getCurrentPoint().x
        }
    }

    fun teachInZ() {
        scope.launch {
            cycleParametersState.zEnd.value = useCase.getCurrentPoint().z
        }
    }
}