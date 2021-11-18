package screen.composables

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumTextField(value: String, enabled: Boolean, modifier: Modifier = Modifier, valueChanged: (String) -> Unit) {
    OutlinedTextField(
        enabled = enabled,
        value = value,
        modifier = modifier.width(100.dp),
        onValueChange = { valueChanged(it) },
        shape = RoundedCornerShape(4.dp),
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = Color(0xffd8e6ff),
//                    cursorColor = Color.Black,
//                    disabledLabelColor = Color(0xffd8e6ff),
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}