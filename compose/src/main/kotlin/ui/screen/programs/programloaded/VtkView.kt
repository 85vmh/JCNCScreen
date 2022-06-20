package ui.screen.programs.programloaded

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import vtk.LatheVtkPanel
import vtk.MachineLimits
import vtk.PathElement
import vtk.Point

data class VtkUiState(
    val machineLimits: MachineLimits? = null,
    val toolPosition: Point = Point(0.0, 0.0, 0.0),
    val wcsPosition: Point = Point(0.0, 0.0, 0.0),
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