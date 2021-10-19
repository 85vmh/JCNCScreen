package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
@Preview
fun RootScreenView() {
    val di = localDI()

    val statusRepository by di.instance<CncStatusRepository>()
    val commandRepository by di.instance<CncCommandRepository>()

    val viewModel = remember { RootScreenViewModel(statusRepository, commandRepository, RootScreen.SplashScreen) }

    messageView(viewModel.getMessages())

    when (val scr = viewModel.screen) {
        RootScreen.SplashScreen -> SplashScreenView {
            viewModel.screen = RootScreen.MachineNotHomed
        }
        RootScreen.MachineNotHomed -> MachineNotHomedView()
    }
}

@Composable
fun messageView(message: Flow<String>) {
    val str by message.collectAsState(null)
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp)
            .height(40.dp)
            .background(Color.Gray)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Message is: $str"
        )
    }
}

@Composable
fun SplashScreenView(onFinish: () -> Unit) {
    val finished by flow {
        kotlinx.coroutines.delay(2000L)
        emit(true)
    }.collectAsState(false, Dispatchers.IO)

    if (finished) {
        onFinish()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mind Over CNC",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Powered by LinuxCNC & Kotlin",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MachineNotHomedView() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Machine Not Homed!",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}