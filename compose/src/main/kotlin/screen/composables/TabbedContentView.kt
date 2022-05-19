package screen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class ToolsTabItem(val tabTitle: String) {
    ToolHolders("Tool Holders"),
    LatheTools("Lathe Tools"),
    CuttingInserts("Cutting Inserts");
}

@Composable
fun <E : Enum<E>> TabbedContentView(
    tabs: Array<E>,
    currentTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    text: @Composable (E) -> Unit,
    content: @Composable (E) -> Unit
) {
    val item = tabs[currentTabIndex]
    Column(
        modifier = modifier,
    ) {
        TabRow(selectedTabIndex = currentTabIndex) {
            tabs.forEachIndexed { index, tabItem ->
                Tab(
                    selected = currentTabIndex == index,
                    onClick = {
                        onTabSelected.invoke(index)
                    },
                    text = { text(tabItem) }
                )
            }
        }
        content(item)
    }
}