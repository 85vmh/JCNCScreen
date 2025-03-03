package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.toFixedDigitsString
import screen.uimodel.InputType
import screen.uimodel.NumericInputs

@Composable
fun NumericInputField(
    numericValue: String,
    inputType: InputType,
    modifier: Modifier = Modifier,
    valueChanged: (String) -> Unit
) {
    var numPadState by remember { mutableStateOf<NumPadState?>(null) }
    val numInputParameters = NumericInputs.entries[inputType]!!

    BasicTextField(
        textStyle = TextStyle(fontSize = 16.sp),
        readOnly = true,
        enabled = false,
        value = numericValue.toDouble().toFixedDigitsString(numInputParameters.maxDecimalPlaces),
        singleLine = true,
        modifier = modifier.width(100.dp)
            .clickable { numPadState = NumPadState(numericValue.toDouble(), numInputParameters) },
        onValueChange = {
            valueChanged(it)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        decorationBox = {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(1f)
                    .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                it()
            }
        }
    )

    numPadState?.let {
        InputDialogView(
            numPadState = it,
            onCancel = { numPadState = null },
            onSubmit = { value ->
                valueChanged(value.toString())
                numPadState = null
            }
        )
    }
}