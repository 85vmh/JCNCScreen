package screen.viewmodel

import codegen.OdProfileGeometry
import codegen.TurningOperation
import screen.uimodel.CutDirection
import screen.uimodel.CuttingStrategy
import screen.uimodel.Wcs
import screen.uimodel.WorkpieceMaterial
import usecase.ConversationalUseCase
import usecase.model.CuttingParametersState
import usecase.model.OdTurningDataState

class OdTurningViewModel(
    private val conversationalUseCase: ConversationalUseCase
) : ViewModel() {

    private val roughingParams = CuttingParametersState(1, 200, 0.25, 2.0)
    private val finishingParams = CuttingParametersState(1, 200, 0.05, 1.0)

    private val odTurning = OdTurningDataState(
        wcs = Wcs.G54,
        cutDirection = CutDirection.Longitudinal,
        material = WorkpieceMaterial.Plastics,
        cuttingStrategy = CuttingStrategy.RoughingAndFinishing,
        roughingParameters = roughingParams,
        finishingParameters = finishingParams,
        toolClearance = 1.0,
        spindleMaxSpeed = 2000,
        xInitial = 20.0,
        xFinal = 8.0,
        zStart = 1.0,
        zEnd = -30.0,
        fillet = 3.0
    )

    fun getOdTurningDataState(): OdTurningDataState {
        return odTurning
    }

    fun processValues() {
        val geometry = OdProfileGeometry(
            xInitial = odTurning.xInitial.value,
            xFinal = odTurning.xFinal.value,
            zStart = odTurning.zStart.value,
            zEnd = odTurning.zEnd.value,
            filletRadius = odTurning.fillet.value
        )
        val strategies = mutableListOf<TurningOperation.TurningStrategy>()
        odTurning.roughingParameters?.let {
            strategies.add(
                TurningOperation.TurningStrategy.Roughing(
                    toolNumber = it.toolNo.value,
                    cssSpeed = it.cssSpeed.value,
                    maxSpindleSpeed = odTurning.spindleMaxSpeed.value,
                    feedRate = it.feed.value,
                    remainingDistance = odTurning.finishingParameters?.doc?.value ?: 0.0,
                    cuttingIncrement = it.doc.value,
                    retractDistance = odTurning.toolClearance.value,
                    cutType = TurningOperation.CutType.Straight,
                    direction = when (odTurning.cutDirection.value) {
                        CutDirection.Longitudinal -> TurningOperation.TraverseDirection.ZAxis
                        CutDirection.Transversal -> TurningOperation.TraverseDirection.XAxis
                    }
                )
            )
        }
        odTurning.finishingParameters?.let {
            strategies.add(
                TurningOperation.TurningStrategy.Finishing(
                    toolNumber = it.toolNo.value,
                    cssSpeed = it.cssSpeed.value,
                    maxSpindleSpeed = odTurning.spindleMaxSpeed.value,
                    startingDistance = 0.0,
                    endingDistance = 0.0,
                    numberOfPasses = 1,
                    feedRate = it.feed.value
                )
            )
        }

        val turningOperation = TurningOperation(
            profileGeometry = geometry,
            turningStrategies = strategies,
            startingXPosition = odTurning.xInitial.value,
            startingZPosition = odTurning.zStart.value
        )

        conversationalUseCase.processTurningOp(turningOperation)
    }
}