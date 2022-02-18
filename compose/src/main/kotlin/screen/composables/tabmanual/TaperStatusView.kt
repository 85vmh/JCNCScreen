package screen.composables.tabmanual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import extensions.stripZeros
import org.kodein.di.compose.rememberInstance
import screen.composables.SettingStatusRow
import usecase.ManualTurningUseCase

@Composable
fun TaperStatusView(modifier: Modifier = Modifier) {
    val useCase: ManualTurningUseCase by rememberInstance()

    val taperState = useCase.getTaperState()

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
                text = "Taper Turning"
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            SettingStatusRow("Taper angle:", taperState.taperAngle.value.stripZeros(), "degrees", modifier = settingsModifier)
        }
    }
}