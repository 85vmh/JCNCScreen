package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import usecase.model.FeedRateMode
import usecase.model.SpindleControlMode

@Composable
fun TurningSettingsView(onFinish: () -> Unit) {
    var spindleMode by remember { mutableStateOf(SpindleControlMode.RPM) }
    var feedMode by remember { mutableStateOf(FeedRateMode.UNITS_PER_REVOLUTION) }
    var orientedStop by remember { mutableStateOf(false) }

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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 20.sp,
                            text = "Spindle"
                        )
                        Divider(
                            modifier = Modifier,
                            color = Color.DarkGray,
                            thickness = 1.dp
                        )
                        SpindleDisplay(
                            modifier = Modifier,
                            spindleType = spindleMode
                        ) {
                            spindleMode = it
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        CheckBoxSetting("Oriented spindle stop", orientedStop, "150.4", "degrees", {
                            orientedStop = it
                        }, {})
                    }
                }
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier,
                    border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
                    elevation = 8.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 20.sp,
                            text = "Feed"
                        )
                        Divider(
                            modifier = Modifier,
                            color = Color.DarkGray,
                            thickness = 1.dp
                        )
                        FeedDisplay(
                            modifier = Modifier,
                            feedMode = feedMode
                        ) {
                            feedMode = it
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
            NumPadView(modifier = Modifier.fillMaxHeight().width(300.dp), initialValue = "") {

            }
        }

        Button(onClick = {
            onFinish.invoke()
        }) {
            Text("Close settings")
        }
    }

}

@Composable
fun SpindleDisplay(modifier: Modifier, spindleType: SpindleControlMode, onValueChanged: (SpindleControlMode) -> Unit) {
    val alignment = Alignment.CenterVertically
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioBoxSetting("Constant Spindle Speed", spindleType == SpindleControlMode.RPM, "1000", "rev/min", {
            onValueChanged.invoke(SpindleControlMode.RPM)
        }, {})
        RadioBoxSetting("Constant Surface Speed", spindleType == SpindleControlMode.CSS, "250", "mm/min", {
            onValueChanged.invoke(SpindleControlMode.CSS)
        }, {})
        ValueSetting("Maximum spindle speed", spindleType == SpindleControlMode.CSS, "2000", "rev/min") {}
    }
}

@Composable
fun FeedDisplay(modifier: Modifier, feedMode: FeedRateMode, onValueChanged: (FeedRateMode) -> Unit) {
    val alignment = Alignment.CenterVertically
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioBoxSetting("Units per revolution", feedMode == FeedRateMode.UNITS_PER_REVOLUTION, "0.050", "mm/rev", {
            onValueChanged.invoke(FeedRateMode.UNITS_PER_REVOLUTION)
        }, {})
        RadioBoxSetting("Units per minute", feedMode == FeedRateMode.UNITS_PER_MINUTE, "250", "mm/min", {
            onValueChanged.invoke(FeedRateMode.UNITS_PER_MINUTE)
        }, {})
    }
}

@Composable
fun RadioBoxSetting(settingName: String, selected: Boolean, value: String, units: String, onClicked: () -> Unit, onValueChanged: (String) -> Unit) {
    val alignment = Alignment.CenterVertically
    val focusRequester = remember { FocusRequester() }

    Row(verticalAlignment = alignment,
        modifier = Modifier
            .clickable { onClicked() }
            .padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment
        ) {
            RadioButton(
                selected = selected,
                onClick = {
                    onClicked()
                    focusRequester.requestFocus()
                })
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = settingName
            )
        }
        SettingAndUnit(value, units, selected, alignment) {
            onValueChanged(it)
        }
    }
}

@Composable
fun SettingAndUnit(value: String, units: String, selected: Boolean, alignment: Alignment.Vertical, onValueChanged: (String) -> Unit) {
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.width(200.dp)
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
}

@Composable
fun CheckBoxSetting(settingName: String, checked: Boolean, value: String, units: String, onCheckedChange: (Boolean) -> Unit, onValueChanged: (String) -> Unit) {
    val alignment = Alignment.CenterVertically
    Row(verticalAlignment = alignment,
        modifier = Modifier
            .clickable { onCheckedChange.invoke(!checked) }
            .padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment
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
        SettingAndUnit(value, units, checked, alignment) {
            onValueChanged(it)
        }
    }
}

@Composable
fun ValueSetting(settingName: String, active: Boolean, value: String, units: String, onValueChanged: (String) -> Unit) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment,
        modifier = Modifier
            .padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment
        ) {
            Box(modifier = Modifier.size(48.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = settingName
            )
        }
        SettingAndUnit(value, units, active, alignment) {
            onValueChanged(it)
        }
    }
}