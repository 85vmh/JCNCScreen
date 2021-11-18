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
import com.mindovercnc.base.data.FeedMode
import com.mindovercnc.base.data.SpindleMode
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.rememberInstance
import usecase.TurningUseCase

@Composable
@Preview
private fun FeedStatusPreview() {
    FeedStatusView()
}

@Composable
fun FeedStatusView(modifier: Modifier = Modifier) {

    val useCase: TurningUseCase by rememberInstance()

    data class FeedModeAndUnits(val mode: String, val units: String)

    val feed by useCase.feedMode
        .map {
            when (it) {
                FeedMode.UNITS_PER_REVOLUTION -> FeedModeAndUnits("Units per revolution", "mm/rev")
                FeedMode.UNITS_PER_MINUTE -> FeedModeAndUnits("Units per minute", "mm/min")
                else -> FeedModeAndUnits("Undefined", "??/??")
            }
        }.collectAsState(FeedModeAndUnits("Undefined", "??/??"))

    val setFeed by useCase.setFeedRate.collectAsState(0.0)

    val feedOverride by useCase.feedOverride.collectAsState(0.0)

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
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                text = "Feed"
            )
            Divider(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            SettingStatusRow("Mode:", feed.mode, modifier = settingsModifier)
            SettingStatusRow("Set:", setFeed.toString(), feed.units, modifier = settingsModifier)
            SettingStatusRow("Override:", feedOverride.toString(), "%", modifier = settingsModifier)
            SettingStatusRow("Actual:", actualSpeed.toString(), feed.units, modifier = settingsModifier)
        }
    }
}