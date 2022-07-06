package ui.screen.programs.programloaded

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import usecase.model.MachineLimits
import usecase.model.PathElement
import vtk.LatheVtkPanel
import vtk.Point3D

data class VtkUiState(
    val machineLimits: MachineLimits? = null,
    val toolPosition: Point3D = Point3D(0.0, 0.0, 0.0),
    val wcsPosition: Point3D = Point3D(0.0, 0.0, 0.0),
    val pathElements: List<PathElement> = emptyList(),
)

@Composable
fun VtkView(
    state: VtkUiState,
    modifier: Modifier = Modifier
) {
    SwingPanel(
        modifier = modifier,
        factory = {
            LatheVtkPanel()
        },
        update = {
            it.setMachineLimits(state.machineLimits)
            it.setToolPosition(state.toolPosition)
            it.setWcsPosition(state.wcsPosition)
            it.setPathElements(state.pathElements)
            it.repaint() //TODO: double check this
        }
    )
}