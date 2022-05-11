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
fun KeySlotParametersView(viewModel: SimpleCyclesScreenModel, parameters: SimpleCycleParameters.KeySlotParameters) {
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
        CycleParameter(
            parameterLabel = "Key Depth",
            inputType = InputType.KEY_SLOT_DEPTH,
            value = parameters.xEnd,
            teachInLabel = "TeachIn Depth",
            valueChange = { viewModel.setXEnd(it) },
            teachInClicked = { viewModel.teachInXEnd() }
        )
        CycleParameter(
            parameterLabel = "Doc Increment",
            inputType = InputType.DOC,
            value = parameters.doc,
            valueChange = { viewModel.setDoc(it) },
        )
        CycleParameter(
            parameterLabel = "Feed Per Minute",
            inputType = InputType.FEED_PER_MIN,
            value = parameters.feedPerMinute,
            valueChange = { viewModel.setKeySlotCuttingFeed(it) },
        )
    }
}