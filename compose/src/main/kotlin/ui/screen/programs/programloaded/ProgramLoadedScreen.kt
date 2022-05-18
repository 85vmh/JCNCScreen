package ui.screen.programs.programloaded

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import screen.composables.VtkState
import screen.composables.VtkView
import ui.screen.programs.Programs
import vtk.AxesActor
import vtk.vtkActor
import vtk.vtkConeSource
import vtk.vtkPolyDataMapper

class ProgramLoadedScreen : Programs("Program Loaded") {

    @Composable
    override fun Fab() {
        ExtendedFloatingActionButton(
            text = { Text("Start Program") },
            onClick = {

            },
            icon = {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "",
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ProgramLoadedScreenModel>()
        val ss by screenModel.state.collectAsState()

        val state = remember {
            val cone = vtkConeSource()
            cone.SetResolution(8)

            val coneMapper = vtkPolyDataMapper()
            coneMapper.SetInputConnection(cone.GetOutputPort())

            val coneActor = vtkActor()
            coneActor.SetMapper(coneMapper)
            VtkState(coneActor)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            VtkView(state, modifier = Modifier.fillMaxWidth().height(500.dp))
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    state.addActor(AxesActor())
                }
            ) {
                Text("Add Actor")
            }
        }

    }
}