package screen.composables.tabmanual

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.toFixedDigitsString
import screen.composables.NumericInputField
import screen.uimodel.InputType
import usecase.VirtualLimitsUseCase
import usecase.model.VirtualLimitsState

@Composable
fun VirtualLimitsSettingsView(
    viewModel: LimitsSettingsViewModel,
    modifier: Modifier,
) {

    var xMinusValue by viewModel.limitsState.xMinus
    var xPlusValue by viewModel.limitsState.xPlus
    var zMinusValue by viewModel.limitsState.zMinus
    var zPlusValue by viewModel.limitsState.zPlus

    var xMinActive by viewModel.limitsState.xMinusActive
    var xMaxActive by viewModel.limitsState.xPlusActive
    var zMinActive by viewModel.limitsState.zMinusActive
    var zMaxActive by viewModel.limitsState.zPlusActive
    var zMaxToolRelated by viewModel.limitsState.zPlusIsToolRelated

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Limit("X-", xMinActive, xMinusValue,
            activeChange = { xMinActive = it },
            valueChange = {
                xMinusValue = it
            }) {
            viewModel.teachInXMinus()
        }
        Limit("X+", xMaxActive, xPlusValue,
            activeChange = { xMaxActive = it },
            valueChange = {
                xPlusValue = it
            }) {
            viewModel.teachInXPlus()
        }
        Limit("Z-", zMinActive, zMinusValue,
            activeChange = { zMinActive = it },
            valueChange = {
                zMinusValue = it
            }) {
            viewModel.teachInZMinus()
        }
        Limit("Z+", zMaxActive, zPlusValue,
            activeChange = { zMaxActive = it },
            valueChange = {
                zPlusValue = it
            }) {
            viewModel.teachInZPlus()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = zMaxToolRelated,
                onClick = { zMaxToolRelated = true }
            )
            Text("Tool Limit")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = zMaxToolRelated.not(),
                onClick = { zMaxToolRelated = false }
            )
            Text("Slide Limit")
        }
    }
}

@Composable
fun Limit(
    axisDirection: String,
    active: Boolean,
    value: Double,
    activeChange: (Boolean) -> Unit,
    valueChange: (Double) -> Unit,
    teachIn: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = active, activeChange
        )
        Text(
            modifier = Modifier.padding(start = 8.dp), text = axisDirection
        )
        NumericInputField(
            numericValue = value.toFixedDigitsString(),
            inputType = when (axisDirection) {
                "X-" -> InputType.VIRTUAL_LIMIT_X_MINUS
                "X+" -> InputType.VIRTUAL_LIMIT_X_PLUS
                "Z-" -> InputType.VIRTUAL_LIMIT_Z_MINUS
                else -> InputType.VIRTUAL_LIMIT_Z_PLUS
            },
            modifier = Modifier.width(100.dp).padding(start = 16.dp)
        ) {
            valueChange.invoke(it.toDouble())
        }

        Button(
            modifier = Modifier.padding(start = 16.dp),
            enabled = active,
            onClick = teachIn
        ) {
            Text("Teach In $axisDirection")
        }
    }
}

class LimitsSettingsViewModel(
    val useCase: VirtualLimitsUseCase
) {
    val limitsState: VirtualLimitsState = useCase.virtualLimitsState

    fun enterEditMode() {
        useCase.isInEditMode = true
    }

    fun exitEditMode() {
        useCase.isInEditMode = false
    }

    fun save() {
        useCase.saveVirtualLimits(limitsState)
        useCase.isInEditMode = false
    }

    fun teachInXMinus() {
        useCase.teachInXMinus()
    }

    fun teachInXPlus() {
        useCase.teachInXPlus()
    }

    fun teachInZMinus() {
        useCase.teachInZMinus()
    }

    fun teachInZPlus() {
        useCase.teachInZPlus()
    }
}
