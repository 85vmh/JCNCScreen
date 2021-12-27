package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.common.AppTheme
import usecase.ManualTurningUseCase

@Composable
fun ManualTurningView(
    modifier: Modifier,
    turningSettingsClicked: () -> Unit,
) {

    val useCase: ManualTurningUseCase by rememberInstance()

    val scope = rememberCoroutineScope()
//
//    val snackbarHostState = remember{ SnackbarHostState().apply {
//        scope.launch {
//            showSnackbar("Test Mesage", duration = SnackbarDuration.Short)
//        }
//    }}
//    SnackbarHost(snackbarHostState, modifier = Modifier.fillMaxWidth())

    val taperTurningActive by useCase.taperTurningActive.collectAsState()

    //Touch Off: G10 L20
    //Tool Touch Off: G10 L10/11, apoi un G43


    Row {
        NavigationRail(

            modifier = Modifier
                .padding(top = 250.dp)
                .height(230.dp)
                .background(color = AppTheme.colors.backgroundMedium, shape = RoundedCornerShape(16.dp))

        ) {
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.AccountBox, contentDescription = "") },
                label = { Text( if (taperTurningActive) "TaperOn" else "Taper") },
                selectedContentColor = Color(130, 200, 10),
                selected = taperTurningActive,
                onClick = { useCase.toggleTaperTurning() },
            )
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Notifications, contentDescription = "") },
                label = { Text("Radius") },
                selected = false,
                onClick = {},
            )
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "") },
                label = { Text("Stops") },
                selected = false,
                onClick = {},
            )
        }
        Column(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CoordinatesView()
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

//        Row(
//            modifier = Modifier.height(50.dp)
//        ) {
//            HandwheelStatus()
//            JoystickStatus()
//            ToolStatus()

        }
    }

    //}
}