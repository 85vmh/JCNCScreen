package ui.screen.programs.programloaded

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.rememberScreenModel
import org.kodein.di.bindProvider
import screen.composables.VerticalDivider
import screen.composables.common.Settings
import screen.composables.editor.EditorView
import ui.screen.programs.Programs
import java.io.File

private fun File.displayableFilePath(): String {
    val elements = absolutePath.split("/")
    return if (elements.size > 2) {
        "../${elements.subList(elements.size - 2, elements.size).joinToString("/")}"
    } else {
        absolutePath
    }
}

class ProgramLoadedScreen(
    private val file: File
) : Programs("Program Loaded: [${file.displayableFilePath()}]") {

//    @Composable
//    override fun Fab() {
//        ExtendedFloatingActionButton(
//            text = { Text("Start Program") },
//            onClick = {
//
//            },
//            icon = {
//                Icon(
//                    Icons.Default.PlayArrow,
//                    contentDescription = "",
//                )
//            }
//        )
//    }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ProgramLoadedScreenModel> {
            bindProvider { file }
        }
        val settings = Settings()
        val state by screenModel.state.collectAsState()

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                VtkView(
                    state = state.vtkUiState,
                    Modifier.fillMaxWidth().height(400.dp)
                )
                EditorView(
                    model = state.editor,
                    settings = settings,
                    showFileName = false,
                    modifier = Modifier.fillMaxSize()
                )
            }
            VerticalDivider(thickness = 1.dp)
            Column(
                modifier = Modifier.width(420.dp)
            ) {
                state.positionModel?.let {
                    ProgramCoordinatesView(
                        currentWcs = state.currentWcs,
                        positionModel = it
                    )
                }
                StatusView(
                    machineStatus = state.machineStatus,
                    modifier = Modifier.weight(1f)
                )
                ActiveCodesView(
                    activeCodes = state.activeCodes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    screenModel.onActiveCodeClicked(it)
                }
            }
        }
    }
}
