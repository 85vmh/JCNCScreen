package screen.composables.tabmanual

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import screen.composables.CardWithTitle
import screen.composables.NumericInputField
import screen.uimodel.InputType
import screen.uimodel.NumericInputs
import usecase.ManualTurningUseCase
import usecase.model.FeedRateMode
import usecase.model.FeedState
import usecase.model.SpindleControlMode
import usecase.model.SpindleState

@Composable
fun TurningSettingsView(
    viewModel: TurningSettingsViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardWithTitle("Spindle") {
            SpindleDisplay(
                state = viewModel.spindleState,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        CardWithTitle("Feed") {
            FeedDisplay(
                state = viewModel.feedState,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
    }
}

class TurningSettingsViewModel(
    val useCase: ManualTurningUseCase
) {
    val spindleState: SpindleState = useCase.getSpindleState()
    val feedState: FeedState = useCase.getFeedState()

    fun save() {
        useCase.applyFeedSettings(feedState)
        useCase.applySpindleSettings(spindleState)
    }
}

@Composable
fun SpindleDisplay(
    state: SpindleState, modifier: Modifier = Modifier
) {
    var spindleType by state.spindleMode
    var cssValue by state.cssValue
    var rpmValue by state.rpmValue
    var maxSpeed by state.maxCssRpm
    var orientedStop by state.orientedStop
    var stopAngle by state.stopAngle

    val onRpmClicked: () -> Unit = {
        spindleType = SpindleControlMode.RPM
    }

    val onCssClicked: () -> Unit = {
        spindleType = SpindleControlMode.CSS
    }

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioBoxSetting(settingName = "Constant Spindle Speed (RPM)",
            selected = spindleType == SpindleControlMode.RPM,
            value = rpmValue.toString(),
            inputType = InputType.RPM,
            modifier = Modifier.fillMaxWidth().clickable(onClick = onRpmClicked).padding(start = 16.dp),
            onClicked = onRpmClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                rpmValue = doubleValue.toInt()
            })
        RadioBoxSetting(settingName = "Constant Surface Speed (CSS)",
            selected = spindleType == SpindleControlMode.CSS,
            value = cssValue.toString(),
            inputType = InputType.CSS,
            modifier = Modifier.fillMaxWidth().clickable(onClick = onCssClicked).padding(start = 16.dp),
            onClicked = onCssClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                cssValue = doubleValue.toInt()
            })
        ValueSetting(
            settingName = "Maximum spindle speed",
            value = maxSpeed.toString(),
            inputType = InputType.CSS_MAX_RPM
        ) {
            val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
            maxSpeed = doubleValue.toInt()
        }

        CheckBoxSetting(settingName = "Oriented spindle stop",
            checked = orientedStop,
            value = stopAngle,
            inputType = InputType.ORIENTED_STOP,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            onCheckedChange = {
                orientedStop = it
            },
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@CheckBoxSetting
                stopAngle = it
            })
    }
}

@Composable
fun FeedDisplay(
    state: FeedState, modifier: Modifier
) {

    var feedMode by state.feedRateMode
    var unitsPerRev by state.unitsPerRevValue
    var unitsPerMin by state.unitsPerMinValue

    val onUnitsPerRevClicked: () -> Unit = {
        feedMode = FeedRateMode.UNITS_PER_REVOLUTION
    }

    val onUnitsPerMinClicked: () -> Unit = {
        feedMode = FeedRateMode.UNITS_PER_MINUTE
    }

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioBoxSetting(settingName = "Units per revolution",
            selected = feedMode == FeedRateMode.UNITS_PER_REVOLUTION,
            value = unitsPerRev.toString(),
            inputType = InputType.FEED_PER_REV,
            modifier = Modifier.fillMaxWidth().clickable(onClick = onUnitsPerRevClicked).padding(start = 16.dp),
            onClicked = onUnitsPerRevClicked,
            onValueChanged = {
                unitsPerRev = it.toDoubleOrNull() ?: return@RadioBoxSetting
            })
        RadioBoxSetting(settingName = "Units per minute",
            selected = feedMode == FeedRateMode.UNITS_PER_MINUTE,
            value = unitsPerMin.toString(),
            inputType = InputType.FEED_PER_MIN,
            modifier = Modifier.fillMaxWidth().clickable(onClick = onUnitsPerMinClicked).padding(start = 16.dp),
            onClicked = onUnitsPerMinClicked,
            onValueChanged = {
                unitsPerMin = it.toDoubleOrNull() ?: return@RadioBoxSetting
            })
    }
}

@Composable
fun RadioBoxSetting(
    settingName: String,
    selected: Boolean,
    value: String,
    inputType: InputType,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically

    Row(
        verticalAlignment = alignment, modifier = modifier
    ) {
        Row(
            verticalAlignment = alignment, modifier = Modifier.weight(1f)
        ) {
            RadioButton(selected = selected, onClick = {
                onClicked()
            })
            Text(
                modifier = Modifier.padding(start = 16.dp), text = settingName
            )
        }
        NumericInputWithUnit(value, inputType, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun CheckBoxSetting(
    inputType: InputType,
    settingName: String,
    checked: Boolean,
    value: String,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment, modifier = modifier.padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment, modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = checked, onCheckedChange
            )
            Text(
                modifier = Modifier.padding(start = 16.dp), text = settingName
            )
        }
        NumericInputWithUnit(value, inputType, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun ValueSetting(
    settingName: String, value: String, inputType: InputType, onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment, modifier = Modifier.padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment, modifier = Modifier.weight(1f)
        ) {
            Box(modifier = Modifier.size(48.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp), text = settingName
            )
        }
        NumericInputWithUnit(value, inputType, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun NumericInputWithUnit(
    value: String, inputType: InputType, alignment: Alignment.Vertical, modifier: Modifier = Modifier, onValueChanged: (String) -> Unit
) {
    val params = NumericInputs.entries[inputType]!!

    Row(
        verticalAlignment = alignment, modifier = modifier
    ) {
        NumericInputField(
            numericValue = value,
            inputType = inputType,
            modifier = Modifier.width(80.dp)
        ) {
            onValueChanged(it)
        }
        params.unit?.let {
            Text(
                modifier = Modifier.padding(start = 8.dp), text = it
            )
        }
    }
}