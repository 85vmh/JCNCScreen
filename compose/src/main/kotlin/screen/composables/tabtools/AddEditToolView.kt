package screen.composables.tabtools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import org.kodein.di.compose.rememberInstance
import screen.composables.DropDownSetting
import screen.composables.tabconversational.InputSetting
import screen.uimodel.AllowedSpindleDirection
import screen.uimodel.InputType
import screen.uimodel.ToolType
import usecase.ToolsUseCase

@Composable
fun AddEditToolView(
    modifier: Modifier
) {
    val useCase: ToolsUseCase by rememberInstance()
    val toolState = remember { useCase.toolState }

    //insert type
    //feed, doc, css speed

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InputSetting(inputType = InputType.TOOL_NUMBER, value = toolState.toolNo.value.toString()) {
            toolState.toolNo.value = it.toDouble().toInt()
        }

        DropDownSetting(
            modifier = Modifier.width(400.dp),
            settingName = "Tool Type",
            items = ToolType.values().map { it.text },
            dropDownWidth = 150.dp,
            selected = toolState.toolType.value.text,
            onValueChanged = {
                toolState.toolType.value = ToolType.fromString(it)!!
            }
        )

        DropDownSetting(
            modifier = Modifier.width(400.dp),
            settingName = "Allowed Spindle Direction",
            items = AllowedSpindleDirection.values().map { it.text },
            dropDownWidth = 100.dp,
            selected = toolState.spindleDirection.value.text,
            onValueChanged = {
                toolState.spindleDirection.value = AllowedSpindleDirection.fromString(it)!!
            }
        )

        InputSetting(inputType = InputType.TOOL_X_COORDINATE, value = toolState.xOffset.value.toString()) {
            toolState.xOffset.value = it.toDouble()
        }

        InputSetting(inputType = InputType.TOOL_Z_COORDINATE, value = toolState.zOffset.value.toString()) {
            toolState.zOffset.value = it.toDouble()
        }

        when (toolState.toolType.value) {
            ToolType.PROFILING -> ProfilingView(toolState)
            ToolType.PARTING_GROOVING -> PartingGroovingView(toolState)
            ToolType.DRILLING_REAMING -> DrillingReamingView(toolState)
            ToolType.THREADING -> ThreadingView(toolState)
            ToolType.KEY_SLOTTING -> KeySlottingView(toolState)
        }
    }
}