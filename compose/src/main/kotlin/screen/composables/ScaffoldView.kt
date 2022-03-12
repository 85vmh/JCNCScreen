package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import screen.composables.common.AppTheme
import screen.uimodel.BottomNavTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldView(
    screenTitle: String,
    selectedTab: BottomNavTab,
    selectedTool: Int,
    enabledTabs: List<BottomNavTab> = BottomNavTab.values().asList(),
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
            NavigationBar(
                modifier = Modifier.height(60.dp),
            ) {
                BottomNavTab.values().forEach { bottomTab ->
                    val isSelected = selectedTab == bottomTab
                    val isEnabled = bottomTab in enabledTabs
                    val selectedColor = AppTheme.colors.material.secondary
                    BottomNavigationItem(
                        icon = { TabIcon(bottomTab, isSelected, isEnabled, selectedTool) },
                        selected = isSelected,
                        enabled = isEnabled,
                        onClick = { onTabClicked(bottomTab) },
                        label = {
                            Text(
                                text = bottomTab.tabText,
                                color = when {
                                    isSelected -> selectedColor
                                    isEnabled.not() -> AppTheme.colors.textDisabled
                                    else -> Color.Unspecified
                                }
                            )
                        },
                    )
                }
            }
        },
        content = content
    )
}

@Composable
private fun TabIcon(bottomNavTab: BottomNavTab, isSelected: Boolean, isEnabled: Boolean, selectedTool: Int) {
    val iconTint = when {
        isSelected -> AppTheme.colors.material.secondary
        isEnabled.not() -> AppTheme.colors.textDisabled
        else -> LocalContentColor.current
    }
    val badgeText = when (bottomNavTab) {
        BottomNavTab.ToolsOffsets -> "T$selectedTool"
        else -> null
    }
    if (badgeText != null) {
        BadgedBox(
            badge = {
                Row {
                    Badge(
                        containerColor = AppTheme.colors.material.secondary
                    ) {
                        Text(
                            fontSize = 14.sp,
                            text = badgeText
                        )
                    }
                }
            }
        ) {
            Icon(
                tint = iconTint,
                imageVector = bottomNavTab.tabImage,
                contentDescription = ""
            )
        }
    } else {
        Icon(
            tint = iconTint,
            imageVector = bottomNavTab.tabImage,
            contentDescription = ""
        )
    }
}