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
fun VtkView(state: VtkState, modifier: Modifier = Modifier) {
    val actors = state.actors
    SwingPanel(
        modifier = modifier,
        factory = { vtkPanel() },
        update = {
            val renderer = it.GetRenderer()
            renderer.GetActors().RemoveAllItems()
            actors.forEach {
                print("-----add actor $it")
                renderer.AddActor(it)
            }
        }
    )
}

class VtkState(vararg initialActors: vtkProp) : ActionListener {

    val actors = mutableStateListOf(*initialActors)

    fun addActor(actor: vtkProp) {
        println("add actor called: ${actor.javaClass.name}")
        actors += actor
    }

    override fun actionPerformed(e: ActionEvent?) {

    }

}