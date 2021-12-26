package screen.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import usecase.ConversationalUseCase

@Composable
fun ToolsAndOffsetsView(modifier: Modifier) {
    val useCase: ConversationalUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    TabbedContentView(
        modifier = modifier.padding(16.dp),
        tabs = listOf(
            TabItem("Tool Library") { ToolLibraryView() },
            TabItem("Workpiece Offsets") { OffsetsView() }
        )
    )
}
