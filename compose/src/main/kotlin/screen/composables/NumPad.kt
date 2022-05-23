package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import screen.uimodel.InputType
import screen.uimodel.NumInputParameters
import screen.uimodel.NumericInputs

@Composable
fun NumPadView(
    state: NumPadState,
    modifier: Modifier = Modifier,
) {
    var numPadValue by state.stringValueState
    val signKey = state.signKey
    val dotKey = state.dotKey

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NumPadRow(
            "1", "2", "3",
            onClick = { numPadValue += it }
        )
        NumPadRow(
            "4", "5", "6",
            onClick = { numPadValue += it }
        )
        NumPadRow(
            "7", "8", "9",
            onClick = { numPadValue += it }
        )
        NumPadRow(
            signKey, "0", dotKey,
            onClick = {
                when (it) {
                    "+/-" -> state.toggleSign()
                    "." -> state.addDecimalPlace()
                    else -> numPadValue += it
                }
            }
        )
    }
}

class NumPadState(
    initialValue: Double? = null,
    val numInputParameters: NumInputParameters,
    val inputType: InputType? = null,
    val onSubmitAction: (Double) -> Unit = {}
) {
//    private val defaultValue = initialValue?.toFixedDigits(numInputParameters.maxDecimalPlaces)
//        ?: numInputParameters.initialValue.toFixedDigits(numInputParameters.maxDecimalPlaces)

    val stringValueState = mutableStateOf(initialValue?.toString() ?: "")

    val signKey: String
        get() = if (numInputParameters.allowsNegativeValues) "+/-" else ""

    val dotKey: String
        get() = if (numInputParameters.maxDecimalPlaces > 0) "." else ""

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

private val numPadKeyModifier = Modifier
    .padding(horizontal = 16.dp, vertical = 16.dp)
    .size(50.dp)

@Composable
fun NumPadRow(
    vararg keys: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        keys.forEach {
            if (it.isNotEmpty()) {
                NumPadKey(
                    key = it,
                    onClick = onClick,
                    modifier = numPadKeyModifier
                )
            } else {
                Spacer(modifier = modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumPadKey(
    key: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        onClick = {
            onClick(key)
        },
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.secondary
    ) {
        Text(
            modifier = Modifier.fillMaxSize()
                .wrapContentSize(),
            text = key,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
private fun NumPadViewPreview() {
    NumPadView(
        modifier = Modifier.fillMaxSize(),
        state = NumPadState(
            numInputParameters = NumericInputs.entries[InputType.RPM]!!
        )
    )
}