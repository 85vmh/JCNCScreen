package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import com.mindovercnc.base.GCodeRepository
import com.mindovercnc.linuxcnc.LinuxCncHome
import kotlinx.coroutines.flow.update
import screen.composables.VtkUiState
import screen.composables.editor.Editor
import vtk.AxesActor
import vtk.vtkActor
import vtk.vtkConeSource
import vtk.vtkPolyDataMapper
import java.io.File

class ProgramLoadedScreenModel(
    private val gCodeRepository: GCodeRepository,
    file: File
) : StateScreenModel<ProgramLoadedScreenModel.State>(State()) {

    data class State(
        val currentFolder: File? = null,
        val editor: Editor? = null,
        val vtkUiState: VtkUiState = VtkUiState(createActors())
    )

    fun addActor() {
        mutableState.update {
            it.copy(
                vtkUiState = it.vtkUiState + AxesActor()
            )
        }
    }

    init {
        gCodeRepository.parseFile(file)
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