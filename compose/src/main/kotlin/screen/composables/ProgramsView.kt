package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.data.AppFile
import com.mindovercnc.linuxcnc.toAppFile
import org.kodein.di.compose.rememberInstance
import screen.composables.common.Settings
import screen.composables.editor.EditorEmptyView
import screen.composables.editor.EditorView
import screen.composables.filesystem.BreadcrumbView
import screen.composables.filesystem.FileSystemView
import usecase.ProgramsUseCase

@Composable
fun ProgramsView(
    modifier: Modifier
) {
    val useCase: ProgramsUseCase by rememberInstance()
    val fileSystemItem by useCase.currentFileSystemItem.collectAsState(null)
    val currentEditor by useCase.currentEditor.collectAsState(null)

    val settings = Settings()

    Column {
        Row(
            modifier = Modifier.height(50.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            fileSystemItem?.let {
                BreadcrumbView(it) { clickedPath ->
                    useCase.loadFolderContents(clickedPath)
                }
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
        Row(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.Green)
                    .width(400.dp)
            ) {
                fileSystemItem?.let {
                    FileSystemView(it)
                }
            }
            if (currentEditor != null) {
                Column(Modifier.weight(1f)) {
                    EditorView(currentEditor!!, settings)
                }
            } else {
                EditorEmptyView()
            }

            //TODO: why this is not working?
//            currentEditor?.let {
//                Column(Modifier.weight(1f)) {
//                    EditorView(it, settings)
//                }
//            } ?: kotlin.run {
//                EditorEmptyView()
//            }
        }
    }
}
