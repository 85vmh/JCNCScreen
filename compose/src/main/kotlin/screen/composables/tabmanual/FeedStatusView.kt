package screen.composables.tabmanual

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import extensions.toFixedDigitsString
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.rememberInstance
import screen.composables.SettingStatusRow
import usecase.ManualTurningUseCase
import usecase.model.FeedRateMode

@Composable
@Preview
private fun FeedStatusPreview() {
    FeedStatusView()
}

@Composable
fun FeedStatusView(modifier: Modifier = Modifier) {

    val useCase: ManualTurningUseCase by rememberInstance()

    data class FeedModeAndUnits(val mode: String, val units: String)

    var setFeed = 0.0
    val feed = when (useCase.getFeedState().feedRateMode.value) {
        FeedRateMode.UNITS_PER_REVOLUTION -> {
            setFeed = useCase.getFeedState().unitsPerRevValue.value
            FeedModeAndUnits("Units per revolution", "mm/rev")
        }
        FeedRateMode.UNITS_PER_MINUTE -> {
            setFeed = useCase.getFeedState().unitsPerMinValue.value
            FeedModeAndUnits("Units per minute", "mm/min")
        }
        else -> {
            FeedModeAndUnits("Undefined", "??/??")
        }
    }

    val feedOverride by useCase.feedOverride.map { it.toInt() }.collectAsState(0)

    val actualSpeed by useCase.actualFeedRate.collectAsState(0.0)

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    text = "Feed"
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "(${feedOverride}%)"
                )
            }
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            SettingStatusRow("Mode:", feed.mode, modifier = settingsModifier)
            SettingStatusRow("Set feed:", setFeed.toFixedDigitsString(), feed.units, modifier = settingsModifier)
            SettingStatusRow("Actual feed:", actualSpeed.toFixedDigitsString(), feed.units, modifier = settingsModifier)
        }
    }
}