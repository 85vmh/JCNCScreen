package ui.screen.tools.addholder

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mindovercnc.linuxcnc.model.tools.LatheTool
import com.mindovercnc.linuxcnc.model.tools.ToolHolder
import com.mindovercnc.linuxcnc.model.tools.ToolHolderType
import di.rememberScreenModel
import org.kodein.di.bindProvider
import screen.composables.DropDownSetting
import screen.uimodel.InputType
import ui.screen.manual.Manual
import ui.widget.ValueSetting

class AddEditHolderScreen(
    private val toolHolder: ToolHolder? = null
) : Manual(
    when (toolHolder) {
        null -> "Add Tool Holder"
        else -> "Edit Tool Holder #${toolHolder.holderNumber}"
    }
) {

    @Composable
    override fun Actions() {
        val screenModel: AddEditHolderScreenModel = when (toolHolder) {
            null -> rememberScreenModel()
            else -> rememberScreenModel { bindProvider { toolHolder } }
        }

        val navigator = LocalNavigator.currentOrThrow
        IconButton(
            modifier = iconButtonModifier,
            onClick = {
                screenModel.applyChanges()
                navigator.pop()
            }) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",
            )
        }
    }

    @Composable
    override fun Content() {
        val screenModel: AddEditHolderScreenModel = when (toolHolder) {
            null -> rememberScreenModel()
            else -> rememberScreenModel { bindProvider { toolHolder } }
        }

        val state by screenModel.state.collectAsState()
        AddEditHolderContent(
            state,
            onHolderNumber = screenModel::setHolderNumber,
            onHolderType = screenModel::setHolderType,
            onLatheTool = screenModel::setLatheTool
        )
    }
}

@Composable
private fun AddEditHolderContent(
    state: AddEditHolderScreenModel.State,
    onHolderNumber: (Int) -> Unit,
    onHolderType: (ToolHolderType) -> Unit,
    onLatheTool: (LatheTool) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ValueSetting(
            settingName = "Holder #",
            value = (state.holderNumber ?: 0).toString(),
            inputType = InputType.TOOL_HOLDER_NO,
            onValueChanged = {
                val doubleValue = it.toDouble()
                onHolderNumber(doubleValue.toInt())
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropDownSetting(
            modifier = Modifier.fillMaxWidth(),
            settingName = "Holder Type",
            items = ToolHolderType.values().map { it.name },
            dropDownWidth = 150.dp,
            selected = state.type.name,
            onValueChanged = {
                onHolderType(ToolHolderType.valueOf(it))
            }
        )
        if (state.latheToolsList.isNotEmpty()) {
            DropDownSetting(
                modifier = Modifier.width(300.dp),
                settingName = "Lathe Tool",
                items = state.latheToolsList.map { it.toolId.toString() },
                dropDownWidth = 300.dp,
                selected = state.latheToolsList.first().toolId.toString(),
                onValueChanged = { it ->
                    val doubleValue = it.toDouble()
                    val tool = state.latheToolsList.first { it.toolId == doubleValue.toInt() }
                    onLatheTool(tool)
                }
            )
        }
    }

}

@Composable
@Preview
fun AddEditHolderContentPreview() {
    AddEditHolderContent(
        AddEditHolderScreenModel.State(),
        {},
        {},
        {}
    )
}