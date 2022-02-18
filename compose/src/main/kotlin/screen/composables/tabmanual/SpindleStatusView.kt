package screen.composables.tabmanual

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.*
import org.kodein.di.compose.rememberInstance
import screen.composables.SettingStatusRow
import usecase.ManualTurningUseCase
import usecase.model.SpindleControlMode

@Composable
@Preview
private fun SpindleStatusPreview() {
    SpindleStatusView()
}

@Composable
fun SpindleStatusView(
    modifier: Modifier = Modifier
) {
    val useCase: ManualTurningUseCase by rememberInstance()

    data class SpindleModeAndUnits(val mode: String, val value: String, val units: String)

    val spModeWithUnits = when (useCase.getSpindleState().spindleMode.value) {
        SpindleControlMode.CSS -> SpindleModeAndUnits("CSS", useCase.getSpindleState().cssValue.value.toString(), "m/min")
        SpindleControlMode.RPM -> SpindleModeAndUnits("RPM", useCase.getSpindleState().rpmValue.value.toString(), "rev/min")
    }

    val spindleOverride by useCase.spindleOverride.collectAsState(0)

    val actualSpeed by useCase.actualSpindleSpeed.collectAsState(0)
    val orientedStopActive = useCase.getSpindleState().orientedStop.value
    val orientedStopAngle = useCase.getSpindleState().stopAngle.value

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        shadowElevation = 8.dp,
    )
    {
        val settingsModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Spindle"
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "(${spindleOverride}%)"
                )
            }
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            /*
            * RPM
            * - Set RPM: 1500 rev/min
            * - Actual RPM
            * - Stop at:
            *
            * CSS
            * - Set CSS: 200 m/min
            * - Max RPM: 2000 rev/min
            * - Actual RPM
            * - Stop at:
            * */

            SettingStatusRow("Set ${spModeWithUnits.mode}:", spModeWithUnits.value, spModeWithUnits.units, modifier = settingsModifier)
            if (useCase.getSpindleState().spindleMode.value == SpindleControlMode.CSS) {
                SettingStatusRow("Max RPM:", useCase.getSpindleState().maxCssRpm.value.toString(), "rev/min", modifier = settingsModifier)
            }
            SettingStatusRow("Actual RPM:", actualSpeed.toString(), "rev/min", modifier = settingsModifier)
            if (orientedStopActive) {
                SettingStatusRow("Oriented stop:", orientedStopAngle, "degrees", modifier = settingsModifier)
            }
        }
    }
}