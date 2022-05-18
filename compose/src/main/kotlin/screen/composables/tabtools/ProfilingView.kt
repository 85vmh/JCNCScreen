package screen.composables.tabtools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindovercnc.linuxcnc.model.tools.TipOrientation
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
            ToolOrientation(TipOrientation.Position4, false, btnModifier)
            ToolOrientation(TipOrientation.Position8, false, btnModifier)
            ToolOrientation(TipOrientation.Position3, false, btnModifier)
        }
        Row {
            ToolOrientation(TipOrientation.Position5, false, btnModifier)
            ToolOrientation(TipOrientation.Position9, false, btnModifier)
            ToolOrientation(TipOrientation.Position7, false, btnModifier)
        }
        Row {
            ToolOrientation(TipOrientation.Position1, false, btnModifier)
            ToolOrientation(TipOrientation.Position6, false, btnModifier)
            ToolOrientation(TipOrientation.Position2, true, btnModifier)
        }
    }
}

@Composable
fun ToolOrientation(
    orientation: TipOrientation,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val type = if (active) "on" else "off"
    val fileName = "turning_tool_${orientation.orient}_$type.xml"

    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(fileName),
            contentDescription = "",
        )
    }
}