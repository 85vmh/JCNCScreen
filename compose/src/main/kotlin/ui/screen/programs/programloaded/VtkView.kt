package ui.screen.programs.programloaded

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import vtk.*

data class VtkState(
    val vtkPanel: vtkPanel,
    val vtkCamera: vtkCamera,
    val vtkRenderWindow: vtkRenderWindow,
    val vtkRenderer: vtkRenderer,

    val actors: List<vtkActor> = emptyList(),
    val axesActors: List<vtkAxesActor> = emptyList(),
){
    operator fun plus(actor: vtkActor) = copy(
        actors = actors + actor
    )
}

@Composable
fun VtkView(
    state: VtkState,
    modifier: Modifier = Modifier,
    setView1: () -> Unit = {},
    setView2: () -> Unit = {},
    setView3: () -> Unit = {}
) {
    Column {
        SwingPanel(
            modifier = Modifier.fillMaxWidth().height(500.dp),
            factory = {
                state.vtkPanel
            },
            update = {
                state.actors.forEach { actor ->
                    if (actor is vtkCubeAxesActor) {
                        actor.SetCamera(state.vtkCamera)
                        println("----actor: ${actor.hashCode()} camera ${actor.GetCamera().hashCode()}")
                    }
                    state.vtkRenderer.AddActor(actor)
                }
                state.axesActors.forEach { axesActor ->
                    state.vtkRenderer.AddActor(axesActor)
                }
                state.vtkRenderer.ResetCamera()
            }
        )
        Row {
            Button(onClick = setView1) {
                Text("Set View 1")
            }
            Button(onClick = setView2) {
                Text("Set View 2")
            }
            Button(onClick = setView3) {
                Text("Set View 3")
            }
        }
    }
}