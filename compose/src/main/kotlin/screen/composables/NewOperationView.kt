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
fun NewOperationView(
    conversationalOperation: ConversationalOperation,
    modifier: Modifier
) {
    val useCase: ConversationalUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    TabbedContentView(
        modifier = modifier.padding(16.dp),
        tabs = listOf(
            TabItem("Technology Data") { TechnologyDataView() },
            TabItem("Geometry Data") { GeometryData(conversationalOperation) }
        )
    )
}

@Composable
fun GeometryData(conversationalOperation: ConversationalOperation) {
    when (conversationalOperation) {
        ConversationalOperation.OdTurning -> OdTurningGeometryData()
        else -> Text("Geometry Data: ${conversationalOperation.displayableString}")
    }
}
