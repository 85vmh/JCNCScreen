package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.base.GCodeRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import screen.composables.VtkUiState
import screen.composables.editor.Editor
import usecase.ActorsUseCase
import vtk.AxesActor
import vtk.vtkActor
import vtk.vtkConeSource
import vtk.vtkPolyDataMapper
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

    init {
        coroutineScope.launch {
            val lineActors = actorsUseCase.getActors(file)
            mutableState.update {
                it.copy(
                    //vtkUiState = it.vtkUiState.actors.plus(lineActors)
                    vtkUiState = it.vtkUiState + lineActors
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