package screen.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import vtk.*


@Composable
fun VtkView(
    state: VtkUiState,
    modifier: Modifier = Modifier
) {
    SwingPanel(
        modifier = modifier,
        factory = {
            val camera = vtkCamera().apply {
                ParallelProjectionOn()
            }
            vtkPanel().apply {
                GetRenderer().SetActiveCamera(camera)
            }
        },
        update = {
            val renderer = it.GetRenderer()
            val activeCamera = renderer.GetActiveCamera()
//            if (renderer.GetActors().GetNumberOfItems() > 0) {
//                renderer.GetActors().RemoveAllItems()
//            }
            state.actors.forEach { actor ->
                if (actor is MachineActor) {
                    actor.SetCamera(activeCamera)
                }
                renderer.AddActor(actor)
            }
            state.axesActors.forEach { axesActor ->
                renderer.AddActor(axesActor)
            }
            renderer.ResetCamera()
        }
    )
}

data class VtkUiState(
    val actors: List<vtkActor> = emptyList(),
    val axesActors: List<vtkAxesActor> = emptyList(),
) {

    operator fun plus(actor: vtkActor) = copy(
        actors = actors + actor
    )

    operator fun plus(actor: List<vtkActor>) = copy(
        actors = actors + actor
    )

    operator fun plus(actor: vtkAxesActor) = copy(
        axesActors = axesActors + actor
    )

}