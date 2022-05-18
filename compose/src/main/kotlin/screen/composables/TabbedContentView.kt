package screen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

data class TabItem(val tabTitle: String, val tabView: @Composable () -> Unit)

@Composable
fun TabbedContentView(
    tabs: List<TabItem>,
    currentTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {

        TabRow(selectedTabIndex = currentTabIndex) {
            tabs.forEachIndexed { index, tabItem ->
                Tab(selected = currentTabIndex == index,
                    onClick = {
                        onTabSelected.invoke(index)
                    },
                    text = {
                        Text(text = tabItem.tabTitle)
                    })
            }
        }
        tabs[currentTabIndex].tabView.invoke()
    }
}