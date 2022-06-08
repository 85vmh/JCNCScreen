package ui.screen.programs.programloaded

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import di.rememberScreenModel
import org.kodein.di.bindProvider
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

        VtkView(
            state = state.vtkUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
