package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.rememberInstance
import screen.composables.*
import screen.viewmodel.BaseScreenViewModel
import usecase.MessagesUseCase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun BaseScreenView() {

    val statusRepository by rememberInstance<CncStatusRepository>()
    val messagesRepository by rememberInstance<MessagesRepository>()
    val messagesUseCase by rememberInstance<MessagesUseCase>()

    val scope = rememberCoroutineScope {
        Dispatchers.Main
    }
    val viewModel = remember {
        BaseScreenViewModel(scope, statusRepository, messagesRepository, BaseScreen.NotHomedScreen)
    }

    var openDialog by remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderView(messagesUseCase.getAllMessages()) {
                openDialog = true
            }

            if (openDialog) {
                Dialog(
                    title = "Messages",
                    onCloseRequest = { openDialog = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    Text("Here I'll display a list of messages")
                }
            }

            when (val screen = viewModel.screen) {
                BaseScreen.SplashScreen -> SplashScreenView()
                BaseScreen.NotHomedScreen -> NotHomedView()
                BaseScreen.RootScreen -> RootScreenView(
                    turningSettingsClicked = { viewModel.turningSettingsClicked() },
                    toolLibraryClicked = { viewModel.toolLibraryClicked() },
                    offsetsClicked = { viewModel.offsetsClicked() },
                    programsClicked = { viewModel.programsClicked() },
                    conversationalClicked = { viewModel.conversationalClicked() }
                )
                is BaseScreen.TurningSettingsScreen -> TurningSettingsView {
                    viewModel.toRootScreen()
                }
                is BaseScreen.ToolLibraryScreen -> ToolLibraryView {
                    viewModel.toRootScreen()
                }
                is BaseScreen.G5xOffsetsScreen -> OffsetsView {
                    viewModel.toRootScreen()
                }
                is BaseScreen.ProgramsScreen -> ProgramsView {
                    viewModel.toRootScreen()
                }
                is BaseScreen.ConversationalScreen -> ConversationalView {
                    viewModel.toRootScreen()
                }
            }
        }
    }
}



