package screen.composables

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import vtk.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener


@Composable
fun VtkView(state: VtkState, modifier: Modifier = Modifier) {
    val actors = state.actors
    SwingPanel(modifier = modifier, factory = {
//        val cone = vtkConeSource()
//        cone.SetResolution(8)
//
//        val coneMapper = vtkPolyDataMapper()
//        coneMapper.SetInputConnection(cone.GetOutputPort())
//
//        val coneActor = vtkActor()
//        coneActor.SetMapper(coneMapper)
//
//        vtkPanel().apply {
//            GetRenderer().AddActor(coneActor)
//        }

        vtkPanel().apply {
            val renderer = GetRenderer()
            actors.forEach {
                print("-----add actor $it")
                renderer.AddActor(it)
            }
        }
    })
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