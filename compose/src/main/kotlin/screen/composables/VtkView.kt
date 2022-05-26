package screen.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import vtk.vtkActor
import vtk.vtkAxesActor
import vtk.vtkCamera
import vtk.vtkPanel


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
            if(renderer.GetActors().GetNumberOfItems() > 0){
                renderer.GetActors().RemoveAllItems()
            }
            state.actors.forEach { actor ->
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
//    val sourceActor: vtkActor =
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