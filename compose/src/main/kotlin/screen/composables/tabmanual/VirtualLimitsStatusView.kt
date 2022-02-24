package screen.composables.tabmanual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import extensions.stripZeros
import extensions.toFixedDigits
import org.kodein.di.compose.rememberInstance
import screen.composables.SettingStatusRow
import usecase.ManualTurningUseCase
import usecase.VirtualLimitsUseCase

@Composable
fun VirtualLimitsStatusView(modifier: Modifier = Modifier) {
    val useCase: VirtualLimitsUseCase by rememberInstance()

    val virtualLimitsState = useCase.getVirtualLimitsState()

    val xMinus by virtualLimitsState.xMinus
    val xMinusActive by virtualLimitsState.xMinusActive
    val xPlus by virtualLimitsState.xPlus
    val xPlusActive by virtualLimitsState.xPlusActive
    val zMinus by virtualLimitsState.zMinus
    val zMinusActive by virtualLimitsState.zMinusActive
    val zPlus by virtualLimitsState.zPlus
    val zPlusActive by virtualLimitsState.zPlusActive

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        elevation = 16.dp
    ) {
        val settingsModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        Column {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium,
                text = "Virtual Limits"
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            if (xMinusActive) {
                AxisLimit("X-", xMinus)
            }
            if (zMinusActive) {
                AxisLimit("Z-", zMinus)
            }
            if (xPlusActive) {
                AxisLimit("X+", xPlus)
            }
            if (zPlusActive) {
                AxisLimit("Z+", zPlus)
            }

        }
    }
}

@Composable
fun AxisLimit(axisDirection: String, value: Double) {
    Text("$axisDirection : ${value.toFixedDigits()}")
}