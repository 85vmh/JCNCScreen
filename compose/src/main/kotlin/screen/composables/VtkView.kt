package screen.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.window.AwtWindow
import vtk.vtkActor
import vtk.vtkAxesActor
import vtk.vtkPanel


@Composable
fun VtkView(
    state: VtkUiState,
    modifier: Modifier = Modifier
) {
    SwingPanel(
        modifier = modifier,
        factory = {
            vtkPanel()
        },
        update = {
            val renderer = it.GetRenderer()
            renderer.GetActors().RemoveAllItems()
            state.actors.forEach { actor ->
                renderer.AddActor(actor)
            }
            state.axesActors.forEach { axesActor ->
                renderer.AddActor(axesActor)
            }
        }
    )
}

data class VtkUiState(
    val actors: List<vtkActor> = emptyList(),
    val axesActors: List<vtkAxesActor> = emptyList()
) {

    operator fun plus(actor: vtkActor) = copy(
        actors = actors + actor
    )

    operator fun plus(actor: vtkAxesActor) = copy(
        axesActors = axesActors + actor
    )

}