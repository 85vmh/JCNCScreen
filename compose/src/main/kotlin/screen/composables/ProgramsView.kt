package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.common.Settings
import screen.composables.editor.EditorEmptyView
import screen.composables.editor.EditorView
import screen.composables.editor.Editors
import screen.composables.filetree.FileTree
import screen.composables.filetree.FileTreeView
import screen.composables.platform.NcFilesFolder
import usecase.ProgramsUseCase

@Composable
fun ProgramsView(onFinish: () -> Unit) {
    val useCase: ProgramsUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    val model = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(NcFilesFolder, editors),
            settings = Settings()
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Column(
                modifier = Modifier
                    .background(color = Color.Green)
                    .width(300.dp)
            ) {
                FileTreeView(model.fileTree)
            }
            if (model.editors.active != null) {
                Column(Modifier.weight(1f)) {
                    //TODO: add a title and a path here
                    EditorView(model.editors.active!!, model.settings)
                }
            } else {
                EditorEmptyView()
            }
        }



        Button(onClick = {
            onFinish.invoke()
        }) {
            Text("<- Back")
        }
    }
}
