package ui.tab

import TabViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import di.rememberScreenModel
import kotlinx.coroutines.launch
import ui.screen.AppScreen

private val tabs = arrayOf<AppTab<*>>(
    ManualTab,
    ConversationalTab,
    ProgramsTab,
    ToolsTab,
    StatusTab
)

@Suppress("UNCHECKED_CAST")
abstract class AppTab<S : AppScreen>(
    private val rootScreen: S
) : Tab {

    private val iconButtonModifier = Modifier.size(48.dp)

    @OptIn(ExperimentalMaterial3Api::class)
    val drawerState: DrawerState = DrawerState(DrawerValue.Closed)

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    final override fun Content() {
        val tabNavigator = LocalTabNavigator.current
        val viewModel = rememberScreenModel<TabViewModel>()
        val uiState by viewModel.state.collectAsState()

        if (this == StatusTab && uiState.isBottomBarEnabled) {
            StatusTab.previousTab?.let {
                tabNavigator.current = it
                StatusTab.previousTab = null
            }
        }
        if (!uiState.isBottomBarEnabled && this != StatusTab) {
            StatusTab.previousTab = this
            tabNavigator.current = StatusTab
        }

        Navigator(rootScreen) { navigator ->
            val currentScreen = navigator.lastItem as S
            ModalNavigationDrawer(
                drawerContent = {
                    currentScreen.DrawerContent(drawerState)
                },
                modifier = Modifier,
                drawerState = drawerState,
                gesturesEnabled = currentScreen.drawerEnabled,
                drawerShape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
            ) {
                ModalBottomSheetLayout(
                    sheetState = currentScreen.sheetState,
                    sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    sheetContent = {
                        Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                            currentScreen.SheetContent(currentScreen.sheetState)
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = { Text(currentScreen.title.value) },
                                navigationIcon = {
                                    when {
                                        currentScreen.drawerEnabled -> {
                                            val scope = rememberCoroutineScope()
                                            IconButton(
                                                modifier = iconButtonModifier,
                                                onClick = {
                                                    scope.launch { drawerState.open() }
                                                })
                                            {
                                                Icon(Icons.Default.Menu, contentDescription = "")
                                            }
                                        }
                                        navigator.canPop -> {
                                            IconButton(
                                                modifier = iconButtonModifier,
                                                onClick = {
                                                    navigator.pop()
                                                })
                                            {
                                                Icon(Icons.Default.ArrowBack, contentDescription = "")
                                            }
                                        }
                                    }
                                },
                                actions = { currentScreen.Actions() },
                                modifier = Modifier.shadow(elevation = 8.dp)
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier.height(60.dp)
                            ) {
                                tabs.forEach { tab ->
                                    TabNavigationItem(
                                        tab = tab,
                                        enabled = uiState.isBottomBarEnabled || tab == StatusTab,
                                        selected = tab == this@AppTab,
                                        onClick = { tabNavigator.current = tab }
                                    )
                                }
                            }
                        },
                        floatingActionButton = { currentScreen.Fab() }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            CurrentScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(
    tab: Tab,
    enabled: Boolean,
    selected: Boolean,
    onClick: (Tab) -> Unit
) {
    val tabColor = when {
        selected -> Color.Green
        enabled.not() -> Color.LightGray
        else -> Color.Unspecified
    }

    BottomNavigationItem(
        label = {
            Text(
                color = tabColor,
                text = tab.options.title,
            )
        },
        enabled = enabled,
        selected = selected,
        onClick = { onClick(tab) },
        icon = {
            Icon(painter = tab.options.icon!!, contentDescription = "", tint = tabColor)
        },
    )
}

