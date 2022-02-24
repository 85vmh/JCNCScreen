package screen.composables.tabmanual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import usecase.VirtualLimitsUseCase
import usecase.model.VirtualLimitsState

@Composable
fun VirtualLimitsSettingsView(
    viewModel: LimitsSettingsViewModel,
    modifier: Modifier
) {

    var xMinusValue by viewModel.limitsState.xMinus
    var xPlusValue by viewModel.limitsState.xPlus
    var zMinusValue by viewModel.limitsState.zMinus
    var zPlusValue by viewModel.limitsState.zPlus

    var xMinActive by viewModel.limitsState.xMinusActive
    var xMaxActive by viewModel.limitsState.xPlusActive
    var zMinActive by viewModel.limitsState.zMinusActive
    var zMaxActive by viewModel.limitsState.zPlusActive

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Limit("X-", xMinActive, xMinusValue,
            activeChange = { xMinActive = it },
            valueChange = {
                xMinusValue = it
            })
        Limit("X+", xMaxActive, xPlusValue,
            activeChange = { xMaxActive = it },
            valueChange = {
                xPlusValue = it
            })
        Limit("Z-", zMinActive, zMinusValue,
            activeChange = { zMinActive = it },
            valueChange = {
                zMinusValue = it
            })
        Limit("Z+", zMaxActive, zPlusValue,
            activeChange = { zMaxActive = it },
            valueChange = {
                zPlusValue = it
            })
    }
}

@Composable
fun Limit(axisDirection: String, active: Boolean, value: Double, activeChange: (Boolean) -> Unit, valueChange: (Double) -> Unit) {
    Row {
        Checkbox(
            checked = active, activeChange
        )
        Text(
            modifier = Modifier.padding(start = 16.dp), text = axisDirection
        )
        TextField(
            value = value.toString(),
            onValueChange = {
                valueChange.invoke(it.toDouble())
            }
        )
    }
}

class LimitsSettingsViewModel(
    val useCase: VirtualLimitsUseCase
) {
    val limitsState: VirtualLimitsState = useCase.getVirtualLimitsState()

    fun save() {
        useCase.saveVirtualLimits(limitsState)
    }
}
