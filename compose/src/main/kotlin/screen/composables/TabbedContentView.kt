package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

data class TabItem(val tabTitle: String, val tabView: @Composable () -> Unit)

@Composable
fun TabbedContentView(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>
) {
    val tabIndex = remember { mutableStateOf(0) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier,
            border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
            elevation = 8.dp
        ) {
            tabs[tabIndex.value].tabView.invoke()
        }
    }
}