package screen.viewmodel

import usecase.VirtualLimitsUseCase
import usecase.model.VirtualLimitsState

class LimitsSettingsViewModel(
    val useCase: VirtualLimitsUseCase
) {
    val limitsState: VirtualLimitsState = useCase.virtualLimitsState

    fun enterEditMode() {
        useCase.isInEditMode = true
    }

    fun exitEditMode() {
        useCase.isInEditMode = false
    }

    fun save() {
        useCase.saveVirtualLimits(limitsState)
        useCase.isInEditMode = false
    }

    fun teachInXMinus() {
        useCase.teachInXMinus()
    }

    fun teachInXPlus() {
        useCase.teachInXPlus()
    }

    fun teachInZMinus() {
        useCase.teachInZMinus()
    }

    fun teachInZPlus() {
        useCase.teachInZPlus()
    }
}