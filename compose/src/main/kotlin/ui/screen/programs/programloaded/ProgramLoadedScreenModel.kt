package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.base.GCodeRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import screen.composables.VtkUiState
import screen.composables.editor.Editor
import usecase.ActorsUseCase
import vtk.*
import java.io.File

class ProgramLoadedScreenModel(
    private val file: File,
    private val actorsUseCase: ActorsUseCase
) : StateScreenModel<ProgramLoadedScreenModel.State>(State()) {

    data class State(
        val currentFolder: File? = null,
        val editor: Editor? = null,
        val vtkUiState: VtkUiState = VtkUiState(createActors())
    )

    val machineLimits = MachineLimits(
        xMin = 0.0,
        xMax = 300.0,
        yMin = 0.0,
        yMax = 400.0,
        zMin = 0.0,
        zMax = 200.0
    )

    init {
        coroutineScope.launch {
            val lineActors = actorsUseCase.getActors(file)
            mutableState.update {
                it.copy(
                    //vtkUiState = it.vtkUiState.actors.plus(lineActors)
                    vtkUiState = it.vtkUiState + lineActors// + MachineActor(machineLimits)
                )
            }
        }
    }
}

private fun createActors(): List<vtkActor> {
    val cone = vtkConeSource()
    cone.SetResolution(8)

    val coneMapper = vtkPolyDataMapper()
    coneMapper.SetInputConnection(cone.GetOutputPort())

    val coneActor = vtkActor()
    coneActor.SetMapper(coneMapper)
    return listOf(coneActor)
}