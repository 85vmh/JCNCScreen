package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.LengthUnit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.kodein.di.compose.rememberInstance
import screen.uimodel.AxisPosition
import screen.uimodel.RootScreenUiModel

@Composable
fun RootScreenView(turningSettingsClicked: () -> Unit) {
    val viewModel: RootScreenViewModel by rememberInstance()
    val model by viewModel.rootScreenUiModel.collectAsState(null)

    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                model?.let {
                    CoordinateView(it.xAxisPos, it.isDiameterMode)
                    CoordinateView(it.zAxisPos)
                }
            }
        }
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
            }
            Column(
                modifier = Modifier.width(180.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Button(onClick = {

                }) {
                    Text("Tools")
                }
                Button(onClick = {
                }) {
                    Text("Programs")
                }
                Button(onClick = {
                }) {
                    Text("Quick Cycles")
                }
                Button(onClick = {
                }) {
                    Text("Taper Turning")
                }
                Button(onClick = {
                }) {
                    Text("Radius Turning")
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
            HandWheelStatus()
            JoystickStatus()
            ToolStatus()
        }
    }

}

class RootScreenViewModel(
    cncStatusRepository: CncStatusRepository
) {
    val rootScreenUiModel = cncStatusRepository.cncStatusFlow()
        .map {
            RootScreenUiModel(
                xAxisPos = it.toAxisPosition(AxisPosition.Axis.X),
                zAxisPos = it.toAxisPosition(AxisPosition.Axis.Z),
                isDiameterMode = it.isDiameterMode()
            )
        }
}

fun CncStatus.toAxisPosition(axis: AxisPosition.Axis): AxisPosition {
    val value = when (axis) {
        AxisPosition.Axis.X -> motionStatus.trajectoryStatus.currentActualPosition.x
        AxisPosition.Axis.Z -> motionStatus.trajectoryStatus.currentActualPosition.z
    }
    val units = when (taskStatus.programUnits) {
        LengthUnit.MM -> AxisPosition.Units.MM
        LengthUnit.IN -> AxisPosition.Units.IN
        LengthUnit.CM -> AxisPosition.Units.CM
    }
    return AxisPosition(axis, value, if (axis == AxisPosition.Axis.X) value else null, units)
}

fun CncStatus.isDiameterMode() = taskStatus.activeCodes.gCodes.contains(7.0f)