package screen.composables.tabtools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.InputDialogView
import screen.composables.NumPadState
import screen.uimodel.InputType
import screen.uimodel.NumericInputs
import usecase.ToolsUseCase

@Composable
fun ToolOffsetsView(
    modifier: Modifier
) {
    val toolsUseCase: ToolsUseCase by rememberInstance()
    val toolState = remember { toolsUseCase.toolState }

    var openKeyboardState by remember { mutableStateOf<NumPadState?>(null) }

    openKeyboardState?.let { numPadState ->
        InputDialogView(
            numPadState = numPadState,
            onCancel = { openKeyboardState = null },
            onSubmit = {
                println("Value is: $it")
                when (numPadState.inputType) {
                    InputType.TOOL_X_COORDINATE -> {
                        toolsUseCase.toolTouchOffX(it)
                    }
                    InputType.TOOL_Z_COORDINATE -> {
                        toolsUseCase.toolTouchOffZ(it)
                    }
                    else -> Unit
                }
                openKeyboardState = null
            }
        )
    }

    //insert type
    //feed, doc, css speed

    Box(
        modifier = modifier
    ) {
        Image(
            bitmap = useResource("offsets.png") { loadImageBitmap(it) },
            contentDescription = ""
        )

        Column(
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 50.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            TouchOff("X", modifier = Modifier.padding(start = 16.dp)) {
                openKeyboardState = NumPadState(
                    numInputParameters = NumericInputs.entries[InputType.TOOL_X_COORDINATE]!!,
                    inputType = InputType.TOOL_X_COORDINATE
                )
            }

            TouchOff("Z", modifier = Modifier.padding(start = 16.dp)) {
                openKeyboardState = NumPadState(
                    numInputParameters = NumericInputs.entries[InputType.TOOL_Z_COORDINATE]!!,
                    inputType = InputType.TOOL_Z_COORDINATE
                )
            }
        }
    }
}

@Composable
private fun TouchOff(axis: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text("Touch\nOff $axis")
    }
}