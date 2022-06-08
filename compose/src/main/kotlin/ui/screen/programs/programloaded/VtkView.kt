package ui.screen.programs.programloaded

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier,
    setView1: () -> Unit = {},
    setView2: () -> Unit = {},
    setView3: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        SwingPanel(
            modifier = Modifier.fillMaxWidth().height(500.dp),
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
        Row {
            Button(onClick = setView1) {
                Text("Action 1")
            }
            Button(onClick = setView2) {
                Text("Action 2")
            }
            Button(onClick = setView3) {
                Text("Action 3")
            }
        }
    }
}