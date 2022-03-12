package screen.viewmodel

import usecase.AngleFinderUseCase
import usecase.ManualTurningUseCase
import usecase.model.AngleFinderState
import usecase.model.TaperState

class TaperSettingsViewModel(
    private val manualTurningUseCase: ManualTurningUseCase,
    private val angleFinderUseCase: AngleFinderUseCase
) {
    val taperState: TaperState = manualTurningUseCase.getTaperState()
    val finderState: AngleFinderState = angleFinderUseCase.angleFinderState()

    fun applySetAngle() {
        manualTurningUseCase.applyTaperSettings(taperState)
    }

    fun startMeasuringAngle(){
        angleFinderUseCase.startProcedure(AngleFinderUseCase.TraverseAxis.Z)
    }

    fun setMeasuredDistance(value: Double){
        angleFinderUseCase.setMeasuredDistance(value)
    }
}