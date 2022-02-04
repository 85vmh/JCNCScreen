package screen.composables

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
        SpindleControlMode.CSS -> SpindleModeAndUnits("CSS", useCase.getSpindleState().cssValue.value, "m/min")
        SpindleControlMode.RPM -> SpindleModeAndUnits("RPM", useCase.getSpindleState().rpmValue.value, "rev/min")
    }

    val spindleOverride by useCase.spindleOverride.collectAsState(0)

    val actualSpeed by useCase.actualSpindleSpeed.collectAsState(0)

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.secondaryContainer
    )
    {
        val settingsModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        Column {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium,
                text = "Spindle"
            )
            Divider(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            /*
            * RPM
            * - Set RPM: 1500 rev/min
            * - Override %
            * - Actual RPM
            * - Stop at:
            *
            *
            * CSS
            * - Set CSS: 200 m/min
            * - Max RPM: 2000 rev/min
            * - Override %
            * - Actual RPM
            * - Stop at:
            * */


            SettingStatusRow("Set ${spModeWithUnits.mode}:", spModeWithUnits.value, spModeWithUnits.units, modifier = settingsModifier)
            if (useCase.getSpindleState().spindleMode.value == SpindleControlMode.CSS) {
                SettingStatusRow("Max RPM:", useCase.getSpindleState().maxCssRpm.value, "rev/min", modifier = settingsModifier)
            }
            SettingStatusRow("Override:", spindleOverride.toString(), "%", modifier = settingsModifier)
            SettingStatusRow("Actual RPM:", actualSpeed.toString(), "rev/min", modifier = settingsModifier)
            SettingStatusRow("Oriented stop at:", "150", "degrees", modifier = settingsModifier)
        }
    }
}