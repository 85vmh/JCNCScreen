package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.rememberInstance
import screen.composables.*
import screen.viewmodel.BaseScreenViewModel
import usecase.MessagesUseCase

import navigation.AppNavigator
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import screen.uimodel.*
import usecase.ManualTurningUseCase

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun BaseScreenView() {

    val statusRepository by rememberInstance<CncStatusRepository>()
    val messagesRepository by rememberInstance<MessagesRepository>()
    val messagesUseCase by rememberInstance<MessagesUseCase>()

    val appNavigator by rememberInstance<AppNavigator>()

    val scope = rememberCoroutineScope {
        Dispatchers.Main
    }
    val viewModel = remember {
        BaseScreenViewModel(scope, statusRepository, messagesRepository, BaseScreen.NotHomedScreen)
    }

    val kbState = mutableStateOf("")

    val numPadState = remember {
        NumPadState().apply {
            setFieldState(kbState)
        }
    }

//    var openDialog by remember { mutableStateOf(false) }
//    var openKeyboard by remember { mutableStateOf(false) }
//
//    if (openDialog) {
//        Dialog(
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

    //val screen = viewModel.screen

    val selectedTab by appNavigator.currentTab.collectAsState()
    val currentScreen by appNavigator.currentScreen.collectAsState(LoadingScreen)

    Box {
        ScaffoldView(
            screenTitle = currentScreen.title,
            selectedTab = selectedTab,
            onTabClicked = { appNavigator.selectTab(it) },
            navigationIcon = {
                if (currentScreen.isBackEnabled) {
                    IconButton(onClick = { appNavigator.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            },
            actions = { ScreenActions(currentScreen, appNavigator) }
        ) { modifier ->
            ScreenContent(currentScreen, modifier, appNavigator)
        }
    }
}

@Composable
private fun ScreenContent(screen: TabScreen, modifier: Modifier, appNavigator: AppNavigator) {
    val useCase by localDI().instance<ManualTurningUseCase>()
    when (screen) {
        ManualScreen.ManualRootScreen -> {
            ManualTurningView(modifier) {
                val viewModel = TurningSettingsViewModel(useCase)
                appNavigator.navigate(ManualScreen.TurningSettingsScreen(viewModel, screen as ManualScreen))
            }
        }
        is ManualScreen.TurningSettingsScreen -> {
            TurningSettingsView(screen.viewModel, modifier)
        }
        ConversationalScreen.ConversationalRootScreen -> {
            ConversationalView(modifier)
        }
        is ConversationalScreen.NewOperationScreen -> {
            NewOperationView(modifier)
        }
        ProgramsScreen.ProgramsRootScreen -> {
            ProgramsView(modifier)
        }
        ToolsOffsetsScreen.ToolsOffsetsRootScreen -> {
            ToolsAndOffsetsView(modifier)
        }
    }
}

@Composable
private fun ScreenActions(screen: TabScreen, appNavigator: AppNavigator) {
    when (screen) {
        ManualScreen.ManualRootScreen -> {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "",
                )
            }
        }
        is ManualScreen.TurningSettingsScreen -> {
            IconButton(onClick = {
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
        is ProgramsScreen.ProgramsRootScreen -> {}
        is ToolsOffsetsScreen.ToolsOffsetsRootScreen -> {}
    }
}



