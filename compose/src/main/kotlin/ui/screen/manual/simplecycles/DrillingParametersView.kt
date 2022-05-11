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
fun DrillingParametersView(viewModel: SimpleCyclesScreenModel, parameters: SimpleCycleParameters.DrillingParameters) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CycleParameter(
            parameterLabel = "Z End",
            inputType = InputType.Z_END,
            value = parameters.zEnd,
            teachInLabel = "TeachIn Z",
            valueChange = { viewModel.setZEnd(it) },
            teachInClicked = { viewModel.teachInZEnd() }
        )
    }
}