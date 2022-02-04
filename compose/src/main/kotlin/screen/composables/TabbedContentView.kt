package screen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

data class TabItem(val tabTitle: String, val tabView: @Composable () -> Unit)

@Composable
fun TabbedContentView(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>
) {
    val tabIndex = remember { mutableStateOf(0) }
    Column(
        modifier = modifier,
    ) {

        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, tabItem ->
                Tab(selected = tabIndex.value == index,
                    onClick = {
                        tabIndex.value = index
                    },
                    text = {
                        Text(text = tabItem.tabTitle)
                    })
            }
        }
        tabs[tabIndex.value].tabView.invoke()
    }
}