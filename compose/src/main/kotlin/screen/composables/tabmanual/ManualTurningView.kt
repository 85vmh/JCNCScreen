package screen.composables.tabmanual

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.CoordinatesView
import usecase.ManualTurningUseCase
import usecase.VirtualLimitsUseCase

@Composable
fun ManualTurningView(
    modifier: Modifier,
    turningSettingsClicked: () -> Unit,
    taperSettingsClicked: () -> Unit,
    limitsSettingsClicked: () -> Unit
) {

    val manualTurningUseCase: ManualTurningUseCase by rememberInstance()
    val virtualLimitsUseCase: VirtualLimitsUseCase by rememberInstance()

    val taperTurningActive by manualTurningUseCase.taperTurningActive.collectAsState()
    val virtualLimitsActive by virtualLimitsUseCase.isLimitsActive.collectAsState()

    Row {
        NavigationRail(

            modifier = Modifier
                .padding(top = 250.dp)
                .height(300.dp)
                .background(color = Color.Unspecified, shape = RoundedCornerShape(16.dp))

        ) {
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.AccountBox, contentDescription = "") },
                label = { Text("Taper") },
                selected = taperTurningActive,
                onClick = { manualTurningUseCase.toggleTaperTurning() },
            )
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Notifications, contentDescription = "") },
                label = { Text("Radius") },
                selected = false,
                onClick = {},
            )
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Star, contentDescription = "") },
                label = { Text("Limits") },
                selected = virtualLimitsActive,
                onClick = { virtualLimitsUseCase.toggleLimitsActive() },
            )
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Star, contentDescription = "") },
                label = { Text("Position") },
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
            if (taperTurningActive) {
                TaperStatusView(
                    Modifier.width(380.dp)
                        .padding(8.dp)
                        .clickable(onClick = taperSettingsClicked)
                )
            }
            if (virtualLimitsActive) {
                VirtualLimitsStatusView(
                    Modifier.width(380.dp)
                        .padding(8.dp)
                        .clickable(onClick = limitsSettingsClicked)
                )
            }
            Row(
                modifier = Modifier.height(60.dp)
            ) {
                HandwheelStatus()
                JoystickStatus()
            }
        }
    }
}