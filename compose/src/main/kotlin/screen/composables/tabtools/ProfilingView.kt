package screen.composables.tabtools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.data.LatheTool
import screen.composables.tabconversational.InputSetting
import screen.uimodel.InputType
import usecase.model.AddEditToolState

@Composable
fun ProfilingView(
    toolState: AddEditToolState
) {

    InputSetting(inputType = InputType.TIP_RADIUS, value = toolState.tipRadius.value.toString()) {
        toolState.tipRadius.value = it.toDouble()
    }

//    InputSetting(inputType = InputType.TOOL_X_COORDINATE, value = toolState.xOffset.value.toString()) {
//        toolState.xOffset.value = it.toDouble()
//    }
//
//    InputSetting(inputType = InputType.TOOL_Z_COORDINATE, value = toolState.zOffset.value.toString()) {
//        toolState.zOffset.value = it.toDouble()
//    }

    val btnModifier = Modifier.size(100.dp)

    Column {
        Row {
            ToolOrientation(LatheTool.Orientation.Position4, false, btnModifier)
            ToolOrientation(LatheTool.Orientation.Position8, false, btnModifier)
            ToolOrientation(LatheTool.Orientation.Position3, false, btnModifier)
        }
        Row {
            ToolOrientation(LatheTool.Orientation.Position5, false, btnModifier)
            ToolOrientation(LatheTool.Orientation.Position9, false, btnModifier)
            ToolOrientation(LatheTool.Orientation.Position7, false, btnModifier)
        }
        Row {
            ToolOrientation(LatheTool.Orientation.Position1, false, btnModifier)
            ToolOrientation(LatheTool.Orientation.Position6, false, btnModifier)
            ToolOrientation(LatheTool.Orientation.Position2, true, btnModifier)
        }
    }
}

@Composable
fun ToolOrientation(
    orientation: LatheTool.Orientation,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val type = if (active) "on" else "off"
    val fileName = "turning_tool_${orientation.orient}_$type.xml"

    Box(
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(fileName),
            contentDescription = ""
        )
    }
}