package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.PopupAlertDialogProvider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
                Text(
                    text = inputParams.valueDescription,
                    style = MaterialTheme.typography.titleLarge
                )
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
                            Icon(Icons.Default.ArrowBack, contentDescription = "Backspace")
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
        dialogProvider = PopupAlertDialogProvider,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}