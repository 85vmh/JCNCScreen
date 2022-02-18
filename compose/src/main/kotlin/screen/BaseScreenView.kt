package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.currentToolNo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import navigation.AppNavigator
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance
import org.kodein.di.instance
import screen.composables.*
import screen.composables.common.AppTheme
import screen.composables.tabconversational.ConversationalView
import screen.composables.tabconversational.NewOperationView
import screen.composables.tabmanual.*
import screen.composables.tabprograms.ProgramLoadedView
import screen.composables.tabprograms.ProgramsView
import screen.composables.tabstatus.MessagesView
import screen.composables.tabstatus.OffsetsView
import screen.composables.tabstatus.StatusView
import screen.composables.tabtools.AddEditToolView
import screen.composables.tabtools.ToolLibraryView
import screen.uimodel.*
import screen.viewmodel.BaseScreenViewModel
import screen.viewmodel.ConversationalViewModel
import usecase.ConversationalUseCase
import usecase.ManualTurningUseCase
import usecase.OffsetsUseCase
import usecase.ProgramsUseCase

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun BaseScreenView() {

    val statusRepository by rememberInstance<CncStatusRepository>()
    val messagesRepository by rememberInstance<MessagesRepository>()
    val offsetsUseCase by rememberInstance<OffsetsUseCase>()

    val appNavigator by rememberInstance<AppNavigator>()

    val scope = rememberCoroutineScope {
        Dispatchers.Main
    }
    val viewModel = remember {
        BaseScreenViewModel(scope, statusRepository, messagesRepository)
    }

    val selectedTool by statusRepository.cncStatusFlow().map { it.currentToolNo }.collectAsState(0)
    val currentWcs by offsetsUseCase.currentWcs.collectAsState("--")

//    val snackbarHostState = remember {
//        SnackbarHostState().apply {
//            scope.launch {
//                val result = showSnackbar("Test Mesage", duration = SnackbarDuration.Indefinite)
//            }
//        }
//    }
//    SnackbarHost(snackbarHostState, modifier = Modifier.fillMaxWidth())

    //var showSnackBar by remember { mutableStateOf(false) }

    val selectedTab by appNavigator.currentTab.collectAsState()
    val currentScreen by appNavigator.currentScreen.collectAsState(LoadingScreen)
    val iconButtonModifier = Modifier.size(48.dp)

    ScaffoldView(
        screenTitle = currentScreen.title,
        selectedTab = selectedTab,
        selectedTool = selectedTool,
        selectedWcs = currentWcs,
        //enabledTabs = listOf(BottomNavTab.ManualTurning, BottomNavTab.Tools),
        onTabClicked = { appNavigator.selectTab(it) },
        navigationIcon = {
            if (currentScreen.isBackEnabled) {
                IconButton(
                    modifier = iconButtonModifier,
                    onClick = { appNavigator.navigateUp() })
                {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        },
        actions = { ScreenActions(currentScreen, appNavigator, iconButtonModifier) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            ScreenContent(currentScreen, appNavigator)
        }
    }
}

@Composable
private fun ScreenContent(tabScreen: TabScreen, appNavigator: AppNavigator, modifier: Modifier = Modifier) {
    val manualTurningUseCase by localDI().instance<ManualTurningUseCase>()
    val conversationalUseCase by localDI().instance<ConversationalUseCase>()

    when (tabScreen) {
        ManualScreen.ManualRootScreen -> {
            ManualTurningView(
                modifier,
                turningSettingsClicked = {
                    val viewModel = TurningSettingsViewModel(manualTurningUseCase)
                    appNavigator.navigate(
                        ManualScreen.TurningSettingsScreen(
                            viewModel = viewModel,
                            previousScreen = tabScreen as ManualScreen
                        )
                    )
                },
                taperSettingsClicked = {
                    val viewModel = TaperSettingsViewModel(manualTurningUseCase)
                    appNavigator.navigate(
                        ManualScreen.TaperSettingsScreen(
                            viewModel = viewModel,
                            previousScreen = tabScreen as ManualScreen
                        )
                    )
                }
            )
        }
        is ManualScreen.TurningSettingsScreen -> {
            TurningSettingsView(tabScreen.viewModel, modifier)
        }
        is ManualScreen.TaperSettingsScreen -> {
            TaperSettingsView(tabScreen.viewModel, modifier)
        }
        ConversationalScreen.ConversationalRootScreen -> {
            ConversationalView(modifier) {
                val viewModel = ConversationalViewModel(conversationalUseCase)
                appNavigator.navigate(
                    ConversationalScreen.NewOperationScreen(
                        conversationalOperation = it,
                        previousScreen = tabScreen as ConversationalScreen,
                        viewModel = viewModel
                    )
                )
            }
        }
        is ConversationalScreen.NewOperationScreen -> {
            NewOperationView(tabScreen.conversationalOperation, modifier)
        }
        ProgramsScreen.ProgramsRootScreen -> {
            ProgramsView(modifier)
        }
        is ProgramsScreen.ProgramLoadedScreen -> {
            ProgramLoadedView(modifier)
        }
        ToolsScreen.ToolsRootScreen -> {
            ToolLibraryView(modifier)
        }
        is ToolsScreen.AddEditToolScreen -> {
            AddEditToolView(modifier)
        }
        StatusScreen.StatusRootScreen -> {
           StatusView(modifier)
        }
    }
}

@Composable
private fun ScreenActions(screen: TabScreen, appNavigator: AppNavigator, modifier: Modifier = Modifier) {
    val programsUseCase by localDI().instance<ProgramsUseCase>()
    val conversationalUseCase by localDI().instance<ConversationalUseCase>()

    when (screen) {
        ManualScreen.ManualRootScreen -> {}
        is ManualScreen.TurningSettingsScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {
                    screen.viewModel.save()
                    appNavigator.navigateUp()
                }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                )
            }
        }
        is ManualScreen.TaperSettingsScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {
                    screen.viewModel.save()
                    appNavigator.navigateUp()
                }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                )
            }
        }
        is ConversationalScreen.ConversationalRootScreen -> {}
        is ConversationalScreen.NewOperationScreen -> {
            val isInputValid by conversationalUseCase.isInputValid.collectAsState()
            IconButton(
                modifier = modifier,
                enabled = isInputValid,
                onClick = {
                    screen.viewModel.processData()
                }
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "",
                )
            }
        }
        is ProgramsScreen.ProgramsRootScreen -> {
            val currentFile by programsUseCase.currentFile.collectAsState()

            IconButton(
                modifier = modifier,
                enabled = currentFile != null,
                onClick = {
                    if (programsUseCase.loadSelectedProgram()) {
                        appNavigator.navigate(
                            ProgramsScreen.ProgramLoadedScreen(
                                previousScreen = screen as ProgramsScreen,
                                programName = currentFile?.path ?: "None"
                            )
                        )
                    }
                }) {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = "",
                )
            }
        }
        is ToolsScreen.ToolsRootScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {
                    appNavigator.navigate(
                        ToolsScreen.AddEditToolScreen(
                            previousScreen = screen as ToolsScreen
                        )
                    )
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                )
            }
        }
        is ToolsScreen.AddEditToolScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {
                    //screen.viewModel.save()
                    appNavigator.navigateUp()
                }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                )
            }
        }
    }
}



