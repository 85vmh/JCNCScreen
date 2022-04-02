package screen.composables.tabmanual

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun CycleParametersView(
    viewModel: CycleParametersViewModel,
    modifier: Modifier
) {

    when (val parameterState = viewModel.cycleParametersState) {
        is FacingParameterState -> FacingParametersView(viewModel, parameterState)
        is TurningParameterState -> TurningParametersView(viewModel, parameterState)
        is BoringParameterState -> BoringParametersView(viewModel, parameterState)
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