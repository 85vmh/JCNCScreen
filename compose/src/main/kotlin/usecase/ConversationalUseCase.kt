package usecase

import codegen.ConversationalProgram
import codegen.OdProfileGeometry
import codegen.ProfileGeometry
import codegen.TurningOperation
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.FileSystemRepository
import com.mindovercnc.base.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import screen.uimodel.CutDirection
import screen.uimodel.CuttingStrategy
import screen.uimodel.Wcs
import screen.uimodel.WorkpieceMaterial
import usecase.model.CuttingParametersState
import usecase.model.OdTurningDataState
import java.util.*

class ConversationalUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val settingsRepository: SettingsRepository,
    private val fileSystemRepository: FileSystemRepository
) {

    private val inputValid = MutableStateFlow(true)

    val isInputValid = inputValid.asStateFlow()

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
                    startingDistance = 0.0,
                    endingDistance = 0.0,
                    numberOfPasses = 1,
                    feedRate = it.feed.value
                )
            )
        }

        val programName = "Test_od_turning.ngc"
        val convProgram = ConversationalProgram(
            programName = programName,
            creationDate = Date(),
            operations = listOf(
                TurningOperation(
                    profileGeometry = geometry,
                    turningStrategies = strategies,
                    startingXPosition = odTurning.xInitial.value,
                    startingZPosition = odTurning.zStart.value
                )
            )
        )

        val programLines = convProgram.generateGCode()
        programLines.forEach {
            println(it)
        }
        fileSystemRepository.writeProgramLines(programLines, programName)
    }
}