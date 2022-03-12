package screen.composables.tabtools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.InputDialogView
import screen.composables.NumPadState
import screen.uimodel.InputType
import screen.uimodel.NumericInputs
import usecase.OffsetsUseCase
import usecase.ToolsUseCase

@Composable
fun WorkOffsetsView(
    modifier: Modifier
) {
    val toolsUseCase: ToolsUseCase by rememberInstance()
    val offsetsUseCase: OffsetsUseCase by rememberInstance()
    val toolState = remember { toolsUseCase.toolState }

    var openKeyboardState by remember { mutableStateOf<NumPadState?>(null) }

    openKeyboardState?.let { numPadState ->
        InputDialogView(
            numPadState = numPadState,
            onCancel = { openKeyboardState = null },
            onSubmit = {
                println("Value is: $it")
                when (numPadState.inputType) {
                    InputType.WORKPIECE_ZERO_COORDINATE -> {
                        offsetsUseCase.touchOffZ(it)
                    }
                    else -> Unit
                }
                openKeyboardState = null
            }
        )
    }

    Column {
        OffsetsView(Modifier)
        Divider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
        Box(
            modifier = modifier
        ) {
            Image(
                bitmap = useResource("offsets.png") { loadImageBitmap(it) },
                contentDescription = ""
            )

            Button(
                onClick = {
                    openKeyboardState = NumPadState(
                        numInputParameters = NumericInputs.entries[InputType.WORKPIECE_ZERO_COORDINATE]!!,
                        inputType = InputType.WORKPIECE_ZERO_COORDINATE
                    )
                },
                modifier = modifier.align(Alignment.CenterEnd)
            ) {
                Text("Set Workpiece Zero")
            }
        }
    }
}