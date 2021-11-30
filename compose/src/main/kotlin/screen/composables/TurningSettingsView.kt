package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.compose.rememberInstance
import usecase.ManualTurningUseCase
import usecase.model.FeedRateMode
import usecase.model.FeedState
import usecase.model.SpindleControlMode
import usecase.model.SpindleState


@Composable
private fun ManualTurningUseCase.createSpindleState(): State<SpindleState?> {
    return produceState<SpindleState?>(null) {
        value = getSpindleState()
    }
}

@Composable
private fun ManualTurningUseCase.createFeedState(): State<FeedState?> {
    return produceState<FeedState?>(null) {
        value = FeedState(
            defaultFeedRateMode = FeedRateMode.UNITS_PER_REVOLUTION,
            defaultUnitsPerRevValue = 0.150,
            defaultUnitsPerMinValue = 200.0
        )
    }
}

@Composable
fun TurningSettingsView(onFinish: () -> Unit) {
    val useCase: ManualTurningUseCase by rememberInstance()

    val spindleState: SpindleState? by useCase.createSpindleState()
    val feedState: FeedState? by useCase.createFeedState()

    var orientedStop by remember { mutableStateOf(false) }

    if (spindleState == null || feedState == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        TurningSettingsContent(spindleState!!, feedState!!, onFinish)
    }
}

@Composable
fun TurningSettingsContent(spindleState: SpindleState, feedState: FeedState, onFinish: () -> Unit) {
    val numPadState = remember {
        NumPadState().apply {
            setFieldState(
                spindleState.rpmValue
            )
        }
    }

    Column {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier,
                    border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
                    elevation = 8.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            fontSize = 20.sp,
                            text = "Spindle"
                        )
                        Divider(
                            modifier = Modifier,
                            color = Color.DarkGray,
                            thickness = 1.dp
                        )
                        SpindleDisplay(
                            state = spindleState,
                            modifier = Modifier.padding(top = 16.dp),
                        )
//                        CheckBoxSetting(
//                            "Oriented spindle stop",
//                            orientedStop,
//                            "150.4",
//                            "degrees",
//                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
//                            {
//                                orientedStop = it
//                            },
//                            {}
//                        )
                    }
                }
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier,
                    border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
                    elevation = 8.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            fontSize = 20.sp,
                            text = "Feed"
                        )
                        Divider(
                            modifier = Modifier,
                            color = Color.DarkGray,
                            thickness = 1.dp
                        )
                        FeedDisplay(
                            state = feedState,
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
            NumPadView(
                modifier = Modifier.fillMaxHeight().width(300.dp),
                state = numPadState
            )
        }

        Button(onClick = {
            onFinish.invoke()
        }) {
            Text("Close settings")
        }
    }
}

@Composable
fun SpindleDisplay(
    state: SpindleState,
    modifier: Modifier = Modifier
) {
    var spindleType by state.spindleType
    var cssValue by state.cssValue
    var rpmValue by state.rpmValue

    val onRpmClicked: () -> Unit = {
        spindleType = SpindleControlMode.RPM
    }

    val onCssClicked: () -> Unit = {
        spindleType = SpindleControlMode.CSS
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioBoxSetting(
            settingName = "Constant Spindle Speed",
            selected = spindleType == SpindleControlMode.RPM,
            value = cssValue,
            units = "rev/min",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onRpmClicked)
                .padding(start = 16.dp),
            onClicked = onRpmClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                cssValue = it
            }
        )
        RadioBoxSetting(
            settingName = "Constant Surface Speed",
            selected = spindleType == SpindleControlMode.CSS,
            value = rpmValue,
            units = "mm/min",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCssClicked)
                .padding(start = 16.dp),
            onClicked = onCssClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                rpmValue = it
            }
        )
        ValueSetting("Maximum spindle speed", spindleType == SpindleControlMode.CSS, "2000", "rev/min") {}
    }
}

@Composable
fun FeedDisplay(
    state: FeedState,
    modifier: Modifier
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
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioBoxSetting(
            settingName = "Units per revolution",
            selected = feedMode == FeedRateMode.UNITS_PER_REVOLUTION,
            value = unitsPerRev,
            units = "mm/rev",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onUnitsPerRevClicked)
                .padding(start = 16.dp),
            onClicked = onUnitsPerRevClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                unitsPerRev = it
            }
        )
        RadioBoxSetting(
            settingName = "Units per minute",
            selected = feedMode == FeedRateMode.UNITS_PER_MINUTE,
            value = unitsPerMin,
            units = "mm/min",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onUnitsPerMinClicked)
                .padding(start = 16.dp),
            onClicked = onUnitsPerMinClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                unitsPerMin = it
            }
        )
    }
}

@Composable
fun RadioBoxSetting(
    settingName: String,
    selected: Boolean,
    value: String,
    units: String,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically

    Row(
        verticalAlignment = alignment,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            RadioButton(
                selected = selected,
                onClick = {
                    onClicked()
                })
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = settingName
            )
        }
        SettingAndUnit(value, units, selected, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun CheckBoxSetting(
    settingName: String,
    checked: Boolean,
    value: String,
    units: String,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment,
        modifier = modifier.padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = settingName
            )
        }
        SettingAndUnit(value, units, checked, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun ValueSetting(
    settingName: String,
    active: Boolean,
    value: String,
    units: String,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            Box(modifier = Modifier.size(48.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = settingName
            )
        }
        SettingAndUnit(value, units, active, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun SettingAndUnit(
    value: String,
    units: String,
    selected: Boolean,
    alignment: Alignment.Vertical,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = alignment,
        modifier = modifier
    ) {
        NumTextField(value, selected) {
            onValueChanged(it)
        }
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = units
        )
    }
}