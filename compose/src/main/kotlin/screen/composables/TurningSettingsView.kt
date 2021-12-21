package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        value = getFeedState()
    }
}

@Composable
fun TurningSettingsView(onFinish: () -> Unit) {
    val useCase: ManualTurningUseCase by rememberInstance()

    val spindleState: SpindleState = useCase.getSpindleState()
    val feedState: FeedState = useCase.getFeedState()

    TurningSettingsContent(spindleState, feedState) {
        useCase.applyFeedSettings(feedState) //this needs to be called first
        useCase.applySpindleSettings(spindleState)
        onFinish.invoke()
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
                verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(1f).align(Alignment.Top).padding(16.dp)
            ) {
                CardWithTitle("Spindle") {
                    SpindleDisplay(
                        state = spindleState,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                CardWithTitle("Feed") {
                    FeedDisplay(
                        state = feedState, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
            NumPadView(
                modifier = Modifier.fillMaxHeight().width(300.dp), state = numPadState
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).height(50.dp).background(Color(170, 170, 180))
        ) {
            Button(modifier = Modifier.fillMaxHeight(), onClick = {
                onFinish.invoke()
            }) {
                Text("<- Back")
            }

            Button(modifier = Modifier.fillMaxHeight(), onClick = {
                println("Spindle mode: ${spindleState.spindleMode}")
                println("Rpm value ${spindleState.rpmValue}")
                println("css value ${spindleState.cssValue}")
                println("stop active ${spindleState.orientedStop}")
                println("Stop angle ${spindleState.stopAngle}")

                onFinish.invoke()
            }) {
                Text("Apply Changes")
            }
        }
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
            value = rpmValue,
            units = "rev/min",
            modifier = Modifier.fillMaxWidth().clickable(onClick = onRpmClicked).padding(start = 16.dp),
            onClicked = onRpmClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                rpmValue = it
            })
        RadioBoxSetting(settingName = "Constant Surface Speed (CSS)",
            selected = spindleType == SpindleControlMode.CSS,
            value = cssValue,
            units = "m/min",
            modifier = Modifier.fillMaxWidth().clickable(onClick = onCssClicked).padding(start = 16.dp),
            onClicked = onCssClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                cssValue = it
            })
        ValueSetting(settingName = "Maximum spindle speed",
            active = spindleType == SpindleControlMode.CSS,
            value = maxSpeed,
            units = "rev/min",
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
                maxSpeed = it
            })

        CheckBoxSetting(settingName = "Oriented spindle stop",
            checked = orientedStop,
            value = stopAngle,
            units = "degrees",
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
            value = unitsPerRev,
            units = "mm/rev",
            modifier = Modifier.fillMaxWidth().clickable(onClick = onUnitsPerRevClicked).padding(start = 16.dp),
            onClicked = onUnitsPerRevClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                unitsPerRev = it
            })
        RadioBoxSetting(settingName = "Units per minute",
            selected = feedMode == FeedRateMode.UNITS_PER_MINUTE,
            value = unitsPerMin,
            units = "mm/min",
            modifier = Modifier.fillMaxWidth().clickable(onClick = onUnitsPerMinClicked).padding(start = 16.dp),
            onClicked = onUnitsPerMinClicked,
            onValueChanged = {
                val doubleValue = it.toDoubleOrNull() ?: return@RadioBoxSetting
                unitsPerMin = it
            })
    }
}

@Composable
fun RadioBoxSetting(
    settingName: String, selected: Boolean, value: String, units: String, modifier: Modifier = Modifier, onClicked: () -> Unit, onValueChanged: (String) -> Unit
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
        ValueAndUnit(value, units, selected, alignment, modifier = Modifier.width(200.dp)) {
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
        ValueAndUnit(value, units, checked, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun ValueSetting(
    settingName: String, active: Boolean, value: String, units: String, onValueChanged: (String) -> Unit
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
        ValueAndUnit(value, units, active, alignment, modifier = Modifier.width(200.dp)) {
            onValueChanged(it)
        }
    }
}

@Composable
fun ValueAndUnit(
    value: String, units: String?, selected: Boolean, alignment: Alignment.Vertical, modifier: Modifier = Modifier, onValueChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = alignment, modifier = modifier
    ) {
        NumTextField(
            modifier = Modifier.width(80.dp), numValue = value, enabled = selected
        ) {
            onValueChanged(it)
        }
        if (units != null) {
            Text(
                modifier = Modifier.padding(start = 8.dp), text = units
            )
        }
    }
}