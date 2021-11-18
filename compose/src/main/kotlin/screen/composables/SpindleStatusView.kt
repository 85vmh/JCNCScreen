package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.*
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.SpindleMode
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.rememberInstance
import screen.uimodel.AxisPosition
import screen.uimodel.RootScreenUiModel
import usecase.TurningUseCase

@Composable
@Preview
private fun SpindleStatusPreview() {
    SpindleStatusView()
}

@Composable
fun SpindleStatusView(
    modifier: Modifier = Modifier
) {
    val useCase: TurningUseCase by rememberInstance()

    data class SpindleModeAndUnits(val mode: String, val units: String)

    val spindleMode by useCase.spindleMode
        .map {
            when (it) {
                SpindleMode.CSS -> SpindleModeAndUnits("Constant Surface Speed", "mm/min")
                SpindleMode.RPM -> SpindleModeAndUnits("Constant Spindle Speed", "rev/min")
                else -> SpindleModeAndUnits("Undefined", "??/??")
            }
        }.collectAsState(SpindleModeAndUnits("Undefined", "??/??"))

    val setSpeed by useCase.setSpindleSpeed.collectAsState(0.0)

    val spindleOverride by useCase.spindleOverride.collectAsState(0.0)

    val actualSpeed by useCase.actualSpindleSpeed.collectAsState(0.0)


    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        elevation = 8.dp
    )
    {
        val settingsModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        Column {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                text = "Spindle"
            )
            Divider(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            SettingStatusRow("Mode:", spindleMode.mode, modifier = settingsModifier)
            SettingStatusRow("Set:", setSpeed.toString(), spindleMode.units, modifier = settingsModifier)
            SettingStatusRow("Override:", spindleOverride.toString(), "%", modifier = settingsModifier)
            SettingStatusRow("Actual:", actualSpeed.toString(), "rev/min", modifier = settingsModifier)
            SettingStatusRow("Oriented stop at:", "150", "degrees", modifier = settingsModifier)
        }
    }
}