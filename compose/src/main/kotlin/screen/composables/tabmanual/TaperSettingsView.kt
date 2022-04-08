package screen.composables.tabmanual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.toFixedDigitsString
import screen.composables.CardWithTitle
import screen.composables.InputDialogView
import screen.composables.NumPadState
import screen.uimodel.InputType
import screen.uimodel.NumericInputs
import screen.viewmodel.TaperSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaperSettingsView(
    viewModel: TaperSettingsViewModel,
    modifier: Modifier
) {

    var openKeyboardState by remember { mutableStateOf<NumPadState?>(null) }

    openKeyboardState?.let { numPadState ->
        InputDialogView(
            numPadState = numPadState,
            onCancel = { openKeyboardState = null },
            onSubmit = {
                println("Value is: $it")
                when (numPadState.inputType) {
                    InputType.DIAL_INDICATOR_DISTANCE -> {
                        viewModel.setMeasuredDistance(it)
                    }
                    else -> Unit
                }
                openKeyboardState = null
            }
        )
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardWithTitle("Known Angle") {
            ValueSetting(
                settingName = "Taper Angle",
                value = viewModel.taperState.taperAngle.value.toFixedDigitsString(),
                inputType = InputType.TAPER_ANGLE
            ) {
                val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
                viewModel.taperState.taperAngle.value = doubleValue
            }
        }
        CardWithTitle("Unknown Angle") {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    enabled = viewModel.finderState.procedureStarted.value.not(),
                    selected = viewModel.finderState.traverseOnZ.value.not(),
                    onClick = { viewModel.finderState.traverseOnZ.value = false }
                )
                Text("Traverse on X")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    enabled = viewModel.finderState.procedureStarted.value.not(),
                    selected = viewModel.finderState.traverseOnZ.value,
                    onClick = { viewModel.finderState.traverseOnZ.value = true }
                )
                Text("Traverse on Z")
            }

            Button(onClick = {
                viewModel.startMeasuringAngle()
            }) {
                Text("Start Measuring")
            }
            Button(onClick = {
                openKeyboardState = NumPadState(
                    numInputParameters = NumericInputs.entries[InputType.DIAL_INDICATOR_DISTANCE]!!,
                    inputType = InputType.DIAL_INDICATOR_DISTANCE
                )
            }) {
                Text("End Measuring")
            }
        }
    }
}
