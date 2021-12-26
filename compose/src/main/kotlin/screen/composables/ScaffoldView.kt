package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import screen.uimodel.BottomNavTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldView(
    screenTitle: String,
    selectedTab: BottomNavTab,
    onTabClicked: (tab: BottomNavTab) -> Unit,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            SmallCenteredTopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = navigationIcon,
                actions = actions,
                modifier = Modifier.shadow(elevation = 8.dp)
            )
        },
        bottomBar = {
            BottomNavigation(
                elevation = 8.dp,
                modifier = Modifier.height(60.dp),
                backgroundColor = MaterialTheme.colorScheme.surface
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "") },
                    selected = selectedTab == BottomNavTab.ManualTurning,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { onTabClicked(BottomNavTab.ManualTurning) },
                    label = { Text(text = "Manual") },
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Outlined.Search, contentDescription = "") },
                    selected = selectedTab == BottomNavTab.Conversational,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { onTabClicked(BottomNavTab.Conversational) },
                    label = { Text(text = "Conversational") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Outlined.List, contentDescription = "") },
                    selected = selectedTab == BottomNavTab.Programs,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { onTabClicked(BottomNavTab.Programs) },
                    label = { Text(text = "Programs") }
                )
                BottomNavigationItem(
                    icon = {
//                        BadgedBox(
//                            badge = {
//                                Badge {
//                                    Text("1")
//                                }
//                            }
//                        ) {
                        Icon(Icons.Outlined.AccountBox, contentDescription = "")
                        //}
                    },
                    selected = selectedTab == BottomNavTab.ToolsOffsets,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { onTabClicked(BottomNavTab.ToolsOffsets) },
                    label = { Text(text = "Tools & Offsets") }
                )
            }
        },
        content = {
            content.invoke(Modifier.padding(it))
        }
    )
}