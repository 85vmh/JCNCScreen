package screen.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun NumTextField(numValue: String, enabled: Boolean, modifier: Modifier = Modifier, valueChanged: (String) -> Unit) {
    OutlinedTextField(
        enabled = enabled,
        value = numValue,
        singleLine = true,
        modifier = modifier.width(100.dp),
        onValueChange = { valueChanged(it) },
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}