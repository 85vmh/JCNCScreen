package ui.screen.programs.programloaded

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.rememberScreenModel
import kotlinx.coroutines.launch
import org.kodein.di.bindProvider
import screen.composables.VtkView
import ui.screen.programs.Programs
import java.io.File

class ProgramLoadedScreen(
    private val file: File
) : Programs("Program Loaded") {

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
        val screenModel = rememberScreenModel<ProgramLoadedScreenModel> {
            bindProvider { file }
        }
        val state by screenModel.state.collectAsState()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            VtkView(
                state.vtkUiState,
                modifier = Modifier.fillMaxWidth().height(500.dp)
            )
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {

                }
            ) {
                Text("---")
            }
        }
    }
}