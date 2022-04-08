package screen.composables.tabmanual

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.toFixedDigitsString
import screen.composables.NumericInputField
import screen.uimodel.InputType
import screen.viewmodel.CycleParametersViewModel
import usecase.model.BoringParameterState
import usecase.model.FacingParameterState
import usecase.model.TurningParameterState
import androidx.compose.runtime.*
import usecase.model.ThreadingParameterState

@Composable
fun CycleParametersView(
    viewModel: CycleParametersViewModel,
    modifier: Modifier
) {

    when (val parameterState = viewModel.cycleParametersState) {
        is FacingParameterState -> FacingParametersView(viewModel, parameterState)
        is TurningParameterState -> TurningParametersView(viewModel, parameterState)
        is BoringParameterState -> BoringParametersView(viewModel, parameterState)
        is ThreadingParameterState -> ThreadingParametersView(viewModel, parameterState)
    }
}

@Composable
fun FacingParametersView(viewModel: CycleParametersViewModel, parametersState: FacingParameterState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CycleParameter(
            parameterLabel = "X End",
            inputType = InputType.X_END,
            value = parametersState.xEnd.value,
            teachInLabel = "TeachIn X",
            valueChange = { parametersState.xEnd.value = it },
            teachInClicked = { viewModel.teachInX() }
        )
        CycleParameter(
            parameterLabel = "Z End",
            inputType = InputType.Z_END,
            value = parametersState.zEnd.value,
            teachInLabel = "TeachIn Z",
            valueChange = { parametersState.zEnd.value = it },
            teachInClicked = { viewModel.teachInZ() }
        )
        CycleParameter(
            parameterLabel = "Depth of cut",
            inputType = InputType.DOC,
            value = parametersState.doc.value,
            valueChange = { parametersState.doc.value = it },
            teachInClicked = { }
        )

        StepsSliderSample()

    }
}

@Composable
fun StepsSliderSample() {

    Column {

        var sliderPosition by remember { mutableStateOf(0f) }
        val values = listOf(0.5f, 0.8f, 1f, 1.25f, 1.5f, 1.75f, 2f, 2.5f)

        Text(sliderPosition.toString())
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..values.lastIndex.toFloat(),
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            steps = values.size
        )
    }
}

@Composable
fun TurningParametersView(viewModel: CycleParametersViewModel, parametersState: TurningParameterState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CycleParameter(
            parameterLabel = "X End",
            inputType = InputType.X_END,
            value = parametersState.xEnd.value,
            teachInLabel = "TeachIn X",
            valueChange = { parametersState.xEnd.value = it },
            teachInClicked = { viewModel.teachInX() }
        )
        CycleParameter(
            parameterLabel = "Z End",
            inputType = InputType.Z_END,
            value = parametersState.zEnd.value,
            teachInLabel = "TeachIn Z",
            valueChange = { parametersState.zEnd.value = it },
            teachInClicked = { viewModel.teachInZ() }
        )
        CycleParameter(
            parameterLabel = "Depth of cut",
            inputType = InputType.DOC,
            value = parametersState.doc.value,
            valueChange = { parametersState.doc.value = it },
            teachInClicked = { }
        )
        CycleParameter(
            parameterLabel = "Taper Angle",
            inputType = InputType.TAPER_ANGLE,
            value = parametersState.turnAngle.value,
            valueChange = { parametersState.turnAngle.value = it },
            teachInClicked = { }
        )
        CycleParameter(
            parameterLabel = "Fillet Radius",
            inputType = InputType.FILLET_RADIUS,
            value = parametersState.filletRadius.value,
            valueChange = { parametersState.filletRadius.value = it },
            teachInClicked = { }
        )
    }
}

@Composable
fun BoringParametersView(viewModel: CycleParametersViewModel, parametersState: BoringParameterState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CycleParameter(
            parameterLabel = "X End",
            inputType = InputType.X_END,
            value = parametersState.xEnd.value,
            teachInLabel = "TeachIn X",
            valueChange = { parametersState.xEnd.value = it },
            teachInClicked = { viewModel.teachInX() }
        )
        CycleParameter(
            parameterLabel = "Z End",
            inputType = InputType.Z_END,
            value = parametersState.zEnd.value,
            teachInLabel = "TeachIn Z",
            valueChange = { parametersState.zEnd.value = it },
            teachInClicked = { viewModel.teachInZ() }
        )
        CycleParameter(
            parameterLabel = "Depth of cut",
            inputType = InputType.DOC,
            value = parametersState.doc.value,
            valueChange = { parametersState.doc.value = it },
            teachInClicked = { }
        )
        CycleParameter(
            parameterLabel = "Taper Angle",
            inputType = InputType.TAPER_ANGLE,
            value = parametersState.turnAngle.value,
            valueChange = { parametersState.turnAngle.value = it },
            teachInClicked = { }
        )
        CycleParameter(
            parameterLabel = "Fillet Radius",
            inputType = InputType.FILLET_RADIUS,
            value = parametersState.filletRadius.value,
            valueChange = { parametersState.filletRadius.value = it },
            teachInClicked = { }
        )
    }
}

@Composable
fun ThreadingParametersView(viewModel: CycleParametersViewModel, parametersState: ThreadingParameterState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CycleParameter(
            parameterLabel = "Thread Pitch",
            inputType = InputType.THREAD_PITCH,
            value = parametersState.threadPitch.value,
            valueChange = { parametersState.threadPitch.value = it },
            teachInClicked = { }
        )
        CycleParameter(
            parameterLabel = "Z End",
            inputType = InputType.Z_END,
            value = parametersState.zEnd.value,
            teachInLabel = "TeachIn Z",
            valueChange = { parametersState.zEnd.value = it },
            teachInClicked = { viewModel.teachInZ() }
        )
        CycleParameter(
            parameterLabel = "Major Diameter",
            inputType = InputType.THREAD_MAJOR_DIAMETER,
            value = parametersState.majorDiameter.value,
            valueChange = { parametersState.majorDiameter.value = it },
            teachInLabel = "TeachIn X",
            teachInClicked = { viewModel.teachInX() }
        )
        CycleParameter(
            parameterLabel = "Initial DOC",
            inputType = InputType.DOC,
            value = parametersState.doc.value,
            valueChange = { parametersState.doc.value = it },
            teachInClicked = { }
        )
    }
}

@Composable
fun CycleParameter(
    parameterLabel: String,
    inputType: InputType,
    value: Double,
    teachInLabel: String? = null,
    valueChange: (Double) -> Unit,
    teachInClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp), text = parameterLabel
        )
        NumericInputField(
            numericValue = value.toFixedDigitsString(),
            inputType = inputType,
            modifier = Modifier.width(100.dp).padding(start = 16.dp)
        ) {
            valueChange.invoke(it.toDouble())
        }

        teachInLabel?.let {
            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = teachInClicked
            ) {
                Text(teachInLabel)
            }
        }
    }
}