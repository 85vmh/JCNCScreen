package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import extensions.toFixedDigits
import screen.uimodel.InputType
import screen.uimodel.NumInputParameters
import screen.uimodel.NumericInputs

@Composable
@Preview
private fun NumPadViewPreview() {
    NumPadView(
        modifier = Modifier.fillMaxSize(),
        state = NumPadState(numInputParameters = NumericInputs.entries[InputType.RPM]!!)
    )
}

class NumPadState(
    initialValue: Double? = null,
    val numInputParameters: NumInputParameters
) {
    private val defaultValue = initialValue?.toFixedDigits(numInputParameters.maxDecimalPlaces)
        ?: numInputParameters.initialValue.toFixedDigits(numInputParameters.maxDecimalPlaces)

    val stringValueState = mutableStateOf(defaultValue)

    fun toggleSign() {
        stringValueState.value = if (stringValueState.value.startsWith('-')) {
            stringValueState.value.drop(1)
        } else {
            "-${stringValueState.value}"
        }
    }

    fun addDecimalPlace() {
        if (stringValueState.value.contains('.')) {
            return
        }
        stringValueState.value += "."
    }

    fun deleteChar() {
        if (stringValueState.value.isNotEmpty()) {
            stringValueState.value = stringValueState.value.dropLast(1)
        }
    }
}

@Composable
fun NumPadView(
    state: NumPadState,
    modifier: Modifier = Modifier,
) {
    var numPadValue by state.stringValueState
    val signKey = if (state.numInputParameters.allowsNegativeValues) "+/-" else ""
    val dotKey = if (state.numInputParameters.maxDecimalPlaces > 0) "." else ""
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NumPadRow("1", "2", "3") { numPadValue += it }
        NumPadRow("4", "5", "6") { numPadValue += it }
        NumPadRow("7", "8", "9") { numPadValue += it }
        NumPadRow(signKey, "0", dotKey) {
            when (it) {
                "+/-" -> state.toggleSign()
                "." -> state.addDecimalPlace()
                else -> numPadValue += it
            }
        }
    }
}

@Composable
fun NumPadRow(vararg keys: String, onClick: (String) -> Unit) {
    Row(modifier = Modifier) {
        keys.forEach {
            NumPadKey(it, onClick)
        }
    }
}

@Composable
fun NumPadKey(key: String, onClick: (String) -> Unit) {
    val shape = CircleShape
    Box(modifier = Modifier.padding(16.dp)) {
        if (key.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp)
                    .background(Color.Gray, shape = shape)
                    .clip(shape)
                    .clickable { onClick(key) },
                contentAlignment = Alignment.Center
            ) {
                Text(key)
            }
        } else {
            Box(
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp)
            )
        }
    }
}