package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import screen.composables.common.AppTheme
import screen.uimodel.BottomNavTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldView(
    screenTitle: String,
    selectedTab: BottomNavTab,
    selectedTool: Int,
    selectedWcs: String,
    onTabClicked: (tab: BottomNavTab) -> Unit,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            CenterAlignedTopAppBar(
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
                BottomNavTab.values().forEach { bottomTab ->
                    BottomNavigationItem(
                        icon = { TabIcon(bottomTab, selectedTool, selectedWcs) },
                        selected = selectedTab == bottomTab,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        onClick = { onTabClicked(bottomTab) },
                        label = { Text(text = bottomTab.tabText) },
                    )
                }
            }
        },
        content = content
    )
}

@Composable
private fun TabIcon(bottomNavTab: BottomNavTab, selectedTool: Int, selectedWcs: String) {
    when (bottomNavTab) {
        BottomNavTab.ManualTurning,
        BottomNavTab.Conversational,
        BottomNavTab.Programs -> {
            Icon(bottomNavTab.tabImage, contentDescription = "")
        }
        BottomNavTab.Tools -> {
            BadgedBox(
                badge = {
                    Row {
                        Badge(
                            backgroundColor = AppTheme.colors.material.secondary
                        ) {
                            Text(selectedTool.toString())
                        }
                    }
                }
            ) {
                Icon(bottomNavTab.tabImage, contentDescription = "")
            }
        }
        BottomNavTab.Settings -> {
            BadgedBox(
                badge = {
                    Row {
                        Badge(
                            backgroundColor = AppTheme.colors.material.secondary
                        ) {
                            Text(selectedWcs)
                        }
                    }
                }
            ) {
                Icon(bottomNavTab.tabImage, contentDescription = "")
            }
        }
    }
}