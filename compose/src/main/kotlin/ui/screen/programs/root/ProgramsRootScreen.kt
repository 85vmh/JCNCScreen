package ui.screen.programs.root

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import screen.composables.VerticalDivider
import screen.composables.common.Settings
import screen.composables.editor.EditorEmptyView
import screen.composables.editor.EditorView
import ui.screen.programs.Programs
import ui.screen.programs.programloaded.ProgramLoadedScreen
import usecase.model.FileSystemItem

class ProgramsRootScreen : Programs("Programs") {

    @Composable
    override fun Fab() {
        val screenModel = rememberScreenModel<ProgramsRootScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        state.editor?.let {
            ExtendedFloatingActionButton(
                text = { Text("Load Program") },
                onClick = {
                    navigator.push(ProgramLoadedScreen())
                },
                icon = {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "",
                    )
                }
            )
        }
    }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ProgramsRootScreenModel>()
        val state by screenModel.state.collectAsState()

        val settings = Settings()

        Column {
            Row(
                modifier = Modifier.height(50.dp).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.currentFolder?.let {
                    BreadcrumbView(it) { clickedPath ->
                        screenModel.loadFolderContents(clickedPath)
                    }
                }
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .width(400.dp)
                ) {
                    state.currentFolder?.let {
                        FileSystemView(it as FileSystemItem.FolderItem)
                    }
                }
                VerticalDivider()
                if (state.editor != null) {
                    Column(Modifier.weight(1f)) {
                        EditorView(state.editor!!, settings)
                    }
                } else {
                    EditorEmptyView()
                }
            }
        }
    }
}