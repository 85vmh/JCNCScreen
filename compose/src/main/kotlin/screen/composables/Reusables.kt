package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.toFixedDigitsString
import screen.uimodel.InputType
import screen.uimodel.NumericInputs

@Composable
fun ValueSetting(
    settingName: String,
    value: String,
    inputType: InputType,
    onValueChanged: (String) -> Unit
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
    value: String,
    inputType: InputType,
    alignment: Alignment.Vertical,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
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

@Composable
fun CycleParameter(
    parameterLabel: String,
    inputType: InputType,
    value: Double,
    teachInLabel: String? = null,
    valueChange: (Double) -> Unit,
    teachInClicked: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp), text = parameterLabel
        )
        NumericInputField(
            numericValue = value.toFixedDigitsString(),
            inputType = inputType,
            modifier = Modifier.width(100.dp).padding(start = 16.dp)
        ) {
            valueChange.invoke(it.toDouble())
        }

        teachInLabel?.let {
            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = teachInClicked
            ) {
                Text(teachInLabel)
            }
        }
    }
}