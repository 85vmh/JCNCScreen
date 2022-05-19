package screen.composables

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import vtk.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener


@Composable
fun VtkView(
    state: VtkUiState,
    modifier: Modifier = Modifier
) {
    SwingPanel(
        modifier = modifier,
        factory = { vtkPanel() },
        update = {
            val renderer = it.GetRenderer()
            renderer.GetActors().RemoveAllItems()
            state.actors.forEach { actor ->
                print("-----add actor $actor")
                renderer.AddActor(actor)
            }
            state.axesActors.forEach { axesActor ->
                print("-----add axes actor $axesActor")
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