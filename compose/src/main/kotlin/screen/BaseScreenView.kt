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
import org.kodein.di.compose.rememberInstance
import screen.composables.*
import screen.viewmodel.BaseScreenViewModel

import navigation.AppNavigator
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import screen.composables.common.AppTheme
import screen.uimodel.*
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
        BaseScreenViewModel(scope, statusRepository, messagesRepository, BaseScreen.NotHomedScreen)
    }

    val selectedTool by statusRepository.cncStatusFlow().map { it.currentToolNo }.collectAsState(0)
    val currentWcs by offsetsUseCase.currentWcs.collectAsState("")

//    var openDialog by remember { mutableStateOf(false) }
//    var openKeyboard by remember { mutableStateOf(false) }
//
//    if (openDialog) {
//        Dialog(
//
//            title = "Messages",
//            onCloseRequest = { openDialog = false },
//            state = rememberDialogState(position = WindowPosition(Alignment.Center))
//        ) {
//            Text("Here I'll display a list of messages")
//        }
//    }
//
//    if (openKeyboard) {
//        AlertDialog(
//            onDismissRequest = { /*TODO*/ },
//            text = {
//                NumPadView(
//                    modifier = Modifier.fillMaxWidth().fillMaxHeight(), state = numPadState
//                )
//            },
//            buttons = {},
//            shape = RoundedCornerShape(8.dp),
//            modifier = Modifier.height(500.dp).width(300.dp)
//        )
//    }


//    val snackbarHostState = remember {
//        SnackbarHostState().apply {
//            scope.launch {
//                val result = showSnackbar("Test Mesage", duration = SnackbarDuration.Indefinite)
//            }
//        }
//    }
//    SnackbarHost(snackbarHostState, modifier = Modifier.fillMaxWidth())

    //var showSnackBar by remember { mutableStateOf(false) }

    when (viewModel.screen) {
        BaseScreen.NotHomedScreen -> NotHomedView()
        else -> {
            val selectedTab by appNavigator.currentTab.collectAsState()
            val currentScreen by appNavigator.currentScreen.collectAsState(LoadingScreen)
            val iconButtonModifier = Modifier.size(48.dp)

            ScaffoldView(
                screenTitle = currentScreen.title,
                selectedTab = selectedTab,
                selectedTool = selectedTool,
                selectedWcs = currentWcs,
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
                    NotificationsView()
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(tabScreen: TabScreen, appNavigator: AppNavigator, modifier: Modifier = Modifier) {
    val manualTurningUseCase by localDI().instance<ManualTurningUseCase>()
    when (tabScreen) {
        ManualScreen.ManualRootScreen -> {
            ManualTurningView(modifier) {
                val viewModel = TurningSettingsViewModel(manualTurningUseCase)
                appNavigator.navigate(
                    ManualScreen.TurningSettingsScreen(
                        viewModel = viewModel,
                        previousScreen = tabScreen as ManualScreen
                    )
                )
            }
        }
        is ManualScreen.TurningSettingsScreen -> {
            TurningSettingsView(tabScreen.viewModel, modifier)
        }
        ConversationalScreen.ConversationalRootScreen -> {
            ConversationalView(modifier) {
                appNavigator.navigate(
                    ConversationalScreen.NewOperationScreen(
                        conversationalOperation = it,
                        previousScreen = tabScreen as ConversationalScreen
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
        SettingsScreen.SettingsRootScreen -> {
            OffsetsView(modifier)
        }
    }
}

@Composable
private fun ScreenActions(screen: TabScreen, appNavigator: AppNavigator, modifier: Modifier = Modifier) {
    val programsUseCase by localDI().instance<ProgramsUseCase>()

    when (screen) {
        ManualScreen.ManualRootScreen -> {
            IconButton(
                modifier = modifier,
                onClick = {}
            ) {
                BadgedBox(
                    badge = {
                        Row {
                            Badge(
                                backgroundColor = AppTheme.colors.material.secondary
                            ) {
                                Text("1")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "")
                }
            }
        }
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
        is ConversationalScreen.ConversationalRootScreen -> {}
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
                onClick = {}
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                )
            }
        }
    }
}



