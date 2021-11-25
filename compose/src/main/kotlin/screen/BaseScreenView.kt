package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.rememberInstance
import screen.composables.*
import screen.viewmodel.BaseScreenViewModel
import usecase.MessagesUseCase

@Composable
@Preview
fun BaseScreenView() {

    val statusRepository by rememberInstance<CncStatusRepository>()
    val commandRepository by rememberInstance<CncCommandRepository>()
    val messagesUseCase by rememberInstance<MessagesUseCase>()

    val scope = rememberCoroutineScope {
        Dispatchers.Main
    }
    val viewModel = remember {
        BaseScreenViewModel(scope, statusRepository, commandRepository, BaseScreen.NotHomedScreen)
    }

    Box {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MessageView(messagesUseCase.getMessage())

            when (val screen = viewModel.screen) {
                BaseScreen.SplashScreen -> SplashScreenView()
                BaseScreen.NotHomedScreen -> NotHomedView()
                BaseScreen.RootScreen -> RootScreenView {
                    viewModel.turningSettingsClicked()
                }
                is BaseScreen.TurningSettingsScreen -> TurningSettingsView() {
                    viewModel.turningSettingsClose()
                }
            }
        }
    }
}



