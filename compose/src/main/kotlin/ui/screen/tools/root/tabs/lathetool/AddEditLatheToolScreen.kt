package ui.screen.tools.root.tabs.lathetool

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mindovercnc.model.LatheTool
import com.mindovercnc.model.ToolHolderType
import com.mindovercnc.model.ToolType
import di.rememberScreenModel
import org.kodein.di.bindProvider
import screen.composables.DropDownSetting
import screen.uimodel.InputType
import ui.screen.manual.Manual
import ui.widget.ValueSetting

class AddEditLatheToolScreen(
    private val latheTool: LatheTool? = null,
    private val onChanges: () -> Unit
) : Manual(
    when (latheTool) {
        null -> "Add Lathe Tool"
        else -> "Edit Lathe Tool #${latheTool.toolId}"
    }
) {

    @Composable
    override fun Actions() {
        val screenModel: AddEditLatheToolScreenModel = when (latheTool) {
            null -> rememberScreenModel()
            else -> rememberScreenModel { bindProvider { latheTool } }
        }

        val navigator = LocalNavigator.currentOrThrow
        IconButton(
            modifier = iconButtonModifier,
            onClick = {
                screenModel.applyChanges()
                onChanges.invoke()
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
        val screenModel: AddEditLatheToolScreenModel = when (latheTool) {
            null -> rememberScreenModel()
            else -> rememberScreenModel { bindProvider { latheTool } }
        }

        val state by screenModel.state.collectAsState()

        AddEditLatheToolContent(
            state,
            onLatheToolNumber = screenModel::setToolId,
            onToolType = screenModel::setToolType
        )
    }
}

@Composable
private fun AddEditLatheToolContent(
    state: AddEditLatheToolScreenModel.State,
    onLatheToolNumber: (Int) -> Unit,
    onToolType: (ToolType) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ValueSetting(
            settingName = "Lathe Tool ID #",
            value = (state.latheToolId ?: 0).toString(),
            inputType = InputType.TOOL_HOLDER_NO,
            onValueChanged = {
                val doubleValue = it.toDouble()
                onLatheToolNumber(doubleValue.toInt())
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropDownSetting(
            modifier = Modifier.fillMaxWidth(),
            dropDownWidth = 150.dp,
            settingName = "Tool Type",
            items = state.toolTypes,
            selectedItem = state.type,
            onValueChanged = onToolType
        )
    }
}

@Composable
@Preview
fun AddEditLatheToolContentPreview() {
    AddEditLatheToolContent(
        AddEditLatheToolScreenModel.State(),
        {},
        {},
    )
}