package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InputDialogView(
    numPadState: NumPadState,
    onCancel: () -> Unit,
    onSubmit: (Double) -> Unit
) {

    val stringValue by numPadState.stringValueState
    val inputParams = numPadState.numInputParameters

    AlertDialog(
        onDismissRequest = { },
        text = {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(70.dp).background(Color.LightGray),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = stringValue,
                        onValueChange = {},
                        label = { Text(inputParams.valueDescription) },
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
                }
                NumPadView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    state = numPadState
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
                Button(
                    onClick = { onSubmit(stringValue.toDouble()) },
                    enabled = stringValue.toDoubleOrNull() != null
                ) {
                    Text("Submit")
                }
            }
        },
        modifier = Modifier
            .border(border = BorderStroke(1.dp, Color.DarkGray))
            .height(600.dp)
            .width(350.dp)

    )
}