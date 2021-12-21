package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumTextField(numValue: String, enabled: Boolean, modifier: Modifier = Modifier, valueChanged: (String) -> Unit) {
    BasicTextField(
        textStyle = TextStyle(fontSize = 16.sp),
        enabled = enabled,
        value = numValue,
        singleLine = true,
        modifier = modifier.width(100.dp),
        onValueChange = { valueChanged(it) },
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
}