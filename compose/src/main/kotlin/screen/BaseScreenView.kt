package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.currentToolNo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import navigation.AppNavigator
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance
import org.kodein.di.instance
import screen.composables.ScaffoldView
import screen.composables.tabconversational.ConversationalView
import screen.composables.tabconversational.NewOperationView
import screen.composables.tabmanual.*
import screen.composables.tabprograms.ProgramLoadedView
import screen.composables.tabprograms.ProgramsView
import screen.composables.tabstatus.StatusView
import screen.composables.tabtools.AddEditToolView
import screen.composables.tabtools.ToolLibraryView
import screen.composables.tabtools.ToolOffsetsView
import screen.composables.tabtools.WorkOffsetsView
import screen.uimodel.*
import screen.viewmodel.*
import usecase.*

@Composable
@Preview
fun BaseScreenView() {

    val statusRepository by rememberInstance<CncStatusRepository>()
    val messagesRepository by rememberInstance<MessagesRepository>()
    val offsetsUseCase by rememberInstance<OffsetsUseCase>()
    val toolsUseCase by rememberInstance<ToolsUseCase>()

    val appNavigator by rememberInstance<AppNavigator>()

    val scope = rememberCoroutineScope()
    val viewModel = remember {
        BaseScreenViewModel(scope, statusRepository, messagesRepository, appNavigator)
    }

    val selectedTool by toolsUseCase.getCurrentTool().map { it?.toolNo ?: 0 }.collectAsState(0)
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
    val enabledTabs by appNavigator.enabledTabs.collectAsState()

    ScaffoldView(
        screenTitle = currentScreen.title,
        selectedTab = selectedTab,
        selectedTool = selectedTool,
        enabledTabs = enabledTabs,
        onTabClicked = { appNavigator.selectTab(it) },
        navigationIcon = {
            if (currentScreen.isBackEnabled) {
                IconButton(
                    modifier = iconButtonModifier,
                    onClick = {
                        when (currentScreen) {
                            is ManualScreen.LimitsSettingsScreen -> (currentScreen as ManualScreen.LimitsSettingsScreen).exitEditMode()
                            is ManualScreen.CycleParametersScreen -> (currentScreen as ManualScreen.CycleParametersScreen).exitEditMode()
                            else -> Unit
                        }
                        appNavigator.navigateUp()
                    })
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
    val virtualLimitsUseCase by localDI().instance<VirtualLimitsUseCase>()
    val simpleCyclesUseCase by localDI().instance<SimpleCyclesUseCase>()
    val angleFinderUseCase by localDI().instance<AngleFinderUseCase>()
    val scope = rememberCoroutineScope()

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
                    val viewModel = TaperSettingsViewModel(manualTurningUseCase, angleFinderUseCase)
                    appNavigator.navigate(
                        ManualScreen.TaperSettingsScreen(
                            viewModel = viewModel,
                            previousScreen = tabScreen as ManualScreen
                        )
                    )
                },
                limitsSettingsClicked = {
                    val viewModel = LimitsSettingsViewModel(virtualLimitsUseCase)
                    appNavigator.navigate(
                        ManualScreen.LimitsSettingsScreen(
                            viewModel = viewModel,
                            previousScreen = tabScreen as ManualScreen
                        )
                    )
                },
                simpleCyclesClicked = {
                    appNavigator.navigate(
                        ManualScreen.SimpleCyclesScreen(
                            previousScreen = tabScreen as ManualScreen
                        )
                    )
                },
                simpleCycleClicked = {

                }
            )
        }
        is ManualScreen.TurningSettingsScreen -> {
            TurningSettingsView(tabScreen.viewModel, modifier)
        }
        is ManualScreen.SimpleCyclesScreen -> {
            SimpleCyclesView(modifier) {
                val viewModel = CycleParametersViewModel(
                    scope = scope,
                    simpleCycle = it,
                    useCase = simpleCyclesUseCase
                )
                viewModel.enterEditMode()
                appNavigator.navigate(
                    ManualScreen.CycleParametersScreen(
                        viewModel = viewModel,
                        simpleCycle = it,
                        previousScreen = tabScreen as ManualScreen
                    )
                )
            }
        }
        is ManualScreen.CycleParametersScreen -> {
            CycleParametersView(tabScreen.viewModel, modifier)
        }
        is ManualScreen.TaperSettingsScreen -> {
            TaperSettingsView(tabScreen.viewModel, modifier)
        }
        is ManualScreen.LimitsSettingsScreen -> {
            tabScreen.viewModel.enterEditMode()
            VirtualLimitsSettingsView(tabScreen.viewModel, modifier)
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
        ToolsOffsetsScreen.ToolsRootScreen -> {
            ToolLibraryView(modifier)
        }
        is ToolsOffsetsScreen.AddEditToolScreen -> {
            AddEditToolView(modifier)
        }
        is ToolsOffsetsScreen.ToolOffsetsScreen -> {
            ToolOffsetsView(modifier)
        }
        is ToolsOffsetsScreen.WorkOffsetsScreen -> {
            WorkOffsetsView(modifier)
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
                    screen.viewModel.applySetAngle()
                    appNavigator.navigateUp()
                }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                )
            }
        }
        is ManualScreen.LimitsSettingsScreen -> {
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
        is ManualScreen.CycleParametersScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {
                    screen.viewModel.save()
                    appNavigator.navigate(ManualScreen.ManualRootScreen)
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
        is ToolsOffsetsScreen.ToolsRootScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {
                    appNavigator.navigate(
                        ToolsOffsetsScreen.AddEditToolScreen(
                            previousScreen = screen as ToolsOffsetsScreen
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
        is ToolsOffsetsScreen.ToolOffsetsScreen,
        is ToolsOffsetsScreen.WorkOffsetsScreen,
        is ToolsOffsetsScreen.AddEditToolScreen -> {
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



