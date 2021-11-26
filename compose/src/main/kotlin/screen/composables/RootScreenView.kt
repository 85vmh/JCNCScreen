package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance
import usecase.TurningUseCase

@Composable
fun RootScreenView(turningSettingsClicked: () -> Unit) {

    val useCase: TurningUseCase by rememberInstance()
    val scope = rememberCoroutineScope()
//
//    val snackbarHostState = remember{ SnackbarHostState().apply {
//        scope.launch {
//            showSnackbar("Test Mesage", duration = SnackbarDuration.Short)
//        }
//    }}

    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CoordinatesView()
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    //.width(IntrinsicSize.Max)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SpindleStatusView(
                        Modifier.width(380.dp)
                            .padding(8.dp)
                            .clickable(onClick = turningSettingsClicked)

                    )
                    FeedStatusView(
                        Modifier.width(380.dp)
                            .padding(8.dp)
                            .clickable(onClick = turningSettingsClicked)
                    )
                }
                //SnackbarHost(snackbarHostState, modifier = Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier.width(180.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Button(onClick = {
                    useCase.executeMdi("G96 D2500 S250")
                }) {
                    Text("Set CSS Mode")
                }

                Button(onClick = {
                    useCase.executeMdi("G97 S500")
                }) {
                    Text("Set RPM Mode")
                }
                Button(onClick = {
                    useCase.executeMdi("G95 F0.350")
                }) {
                    Text("UnitsPerRev")
                }
                Button(onClick = {
                    useCase.executeMdi("G94 F200")
                }) {
                    Text("UnitsPerMin")
                }
                Button(onClick = {
                    useCase.toggleTaperTurning()
                }) {
                    Text("Taper Turning")
                }
            }
        }
        Divider(
            color = Color.DarkGray,
            thickness = 1.dp
        )
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            HandwheelStatus()
            JoystickStatus()
            ToolStatus()
        }
    }
}