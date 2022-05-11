package ui.screen.manual.simplecycles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import screen.composables.tabmanual.CycleParameter
import screen.uimodel.InputType
import ui.screen.manual.simplecycles.SimpleCyclesScreenModel
import usecase.model.SimpleCycleParameters

@Composable
fun BoringParametersView(viewModel: SimpleCyclesScreenModel, parametersState: SimpleCycleParameters.BoringParameters) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CycleParameter(
            parameterLabel = "X End",
            inputType = InputType.X_END,
            value = parametersState.xEnd,
            teachInLabel = "TeachIn X",
            valueChange = { viewModel.setXEnd(it) },
            teachInClicked = { viewModel.teachInXEnd() }
        )
        CycleParameter(
            parameterLabel = "Z End",
            inputType = InputType.Z_END,
            value = parametersState.zEnd,
            teachInLabel = "TeachIn Z",
            valueChange = { viewModel.setZEnd(it) },
            teachInClicked = { viewModel.teachInZEnd() }
        )
        CycleParameter(
            parameterLabel = "Depth of cut",
            inputType = InputType.DOC,
            value = parametersState.doc,
            valueChange = { viewModel.setDoc(it) },
        )
        CycleParameter(
            parameterLabel = "Taper Angle",
            inputType = InputType.TAPER_ANGLE,
            value = parametersState.taperAngle,
            valueChange = { viewModel.setTaperAngle(it) },
        )
        CycleParameter(
            parameterLabel = "Fillet Radius",
            inputType = InputType.FILLET_RADIUS,
            value = parametersState.filletRadius,
            valueChange = { viewModel.setFilletRadius(it) },
        )
    }
}