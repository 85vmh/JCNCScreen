package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InputDialogView(
    numPadState: NumPadState,
    onCancel: () -> Unit,
    onSubmit: (Double) -> Unit
) {

    val valueAsString by numPadState.stringValueState
    val inputParams = numPadState.numInputParameters

    AlertDialog(
        onDismissRequest = { },
        text = {
            Column {
                Text(inputParams.valueDescription)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = valueAsString,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = { numPadState.deleteChar() }
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
                NumPadView(
                    modifier = Modifier.fillMaxWidth(),
                    state = numPadState
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val xx = valueAsString.toDouble()
                    onSubmit(xx)
                },
                enabled = valueAsString.toDoubleOrNull() != null
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        },
        dialogProvider = PopupAlertDialogProvider
    )
}