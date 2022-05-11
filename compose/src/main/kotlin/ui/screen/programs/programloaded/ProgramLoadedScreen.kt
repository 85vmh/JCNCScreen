package ui.screen.programs.programloaded

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.kodein.rememberScreenModel
import ui.screen.programs.Programs

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
        val state by screenModel.state.collectAsState()

    }
}