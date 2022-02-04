package screen.composables.tabconversational

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.TabItem
import screen.composables.TabbedContentView
import usecase.ConversationalUseCase

@Composable
fun NewOperationView(
    conversationalOperation: ConversationalOperation,
    modifier: Modifier
) {
    val useCase: ConversationalUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    when (conversationalOperation) {
        ConversationalOperation.Drilling -> DrillingReamingData()
        ConversationalOperation.Facing -> FacingData()
        ConversationalOperation.Grooving -> GroovingPartingData()
        ConversationalOperation.KeySlot -> SlottingData()
        else -> TabbedContentView(
            modifier = modifier,
            tabs = listOf(
                TabItem("Technology Data") { TechnologyData(conversationalOperation) },
                TabItem("Geometry Data") { GeometryData(conversationalOperation) }
            )
        )
    }
}

@Composable
fun TechnologyData(conversationalOperation: ConversationalOperation) {
    when (conversationalOperation) {
        ConversationalOperation.OdTurning,
        ConversationalOperation.IdTurning -> TurningTechnologyDataView()
        ConversationalOperation.Threading -> ThreadingTechnologyData()
        else -> Text("Geometry Data: ${conversationalOperation.displayableString}")
    }
}

@Composable
fun GeometryData(conversationalOperation: ConversationalOperation) {
    when (conversationalOperation) {
        ConversationalOperation.OdTurning,
        ConversationalOperation.IdTurning -> OdTurningGeometryData()
        ConversationalOperation.Threading -> ThreadingGeometryData()
        else -> Text("Geometry Data: ${conversationalOperation.displayableString}")
    }
}
