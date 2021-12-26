package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.compose.rememberInstance
import screen.composables.common.AppTheme
import usecase.ConversationalUseCase

@Composable
fun ConversationalView(modifier: Modifier) {
    val useCase: ConversationalUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    Text("Conversational Root")

    val selectedTab = MutableStateFlow(0)

    Surface {
        ScrollableTabRow(
            selectedTabIndex = selectedTab.value
        ) {
            ConversationalOperation.values().iterator().forEach {
                Operation(it.displayableString)
            }
        }
    }
}

enum class ConversationalOperation(val displayableString: String) {
    OdTurning("OD Turning"),
    IdTurning("ID Turning"),
    Profiling("Profiling"),
    Facing("Facing"),
    Grooving("Grooving/Parting"),
    Threading("Threading"),
    Drilling("Drilling/Reaming"),
    KeySlot("Slotting"),
}

@Composable
fun Operation(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.width(120.dp).height(120.dp).background(AppTheme.colors.backgroundDark).shadow(8.dp))
        Text(text)
    }
}
