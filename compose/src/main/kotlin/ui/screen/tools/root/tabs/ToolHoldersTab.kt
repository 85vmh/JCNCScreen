package ui.screen.tools.root.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mindovercnc.linuxcnc.model.tools.ToolHolder
import extensions.draggableScroll
import extensions.toFixedDigitsString
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.platform.VerticalScrollbar
import ui.screen.tools.addholder.AddEditHolderScreen
import ui.screen.tools.root.ToolsScreenModel

@Composable
fun ToolHoldersContent(screenModel: ToolsScreenModel) {
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()
    val state by screenModel.state.collectAsState()

    Box(
        modifier = Modifier
    ) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            items(state.toolHolders) { item ->
                HolderView(
                    item = item,
                    isCurrent = item.holderNumber == state.currentTool,
                    onEditClicked = { navigator.push(AddEditHolderScreen(it)) },
                    onDeleteClicked = screenModel::deleteToolHolder,
                    onLoadClicked = screenModel::loadToolHolder
                )
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd).width(30.dp),
            scrollState,
            state.toolHolders.size,
            60.dp
        )
    }
}

@Composable
private fun HolderView(
    item: ToolHolder,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
    onEditClicked: (ToolHolder) -> Unit,
    onDeleteClicked: (ToolHolder) -> Unit,
    onLoadClicked: (ToolHolder) -> Unit
) {
    val nonSelectedModifier = modifier.height(60.dp)
    val selectedModifier = nonSelectedModifier.border(BorderStroke(1.dp, Color.Blue))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = if (isCurrent) selectedModifier else nonSelectedModifier
    ) {
        Text(
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center,
            text = item.holderNumber.toString()
        )
        VerticalDivider()
        Text(
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center,
            text = item.type.name
        )
        VerticalDivider()
        Column(
            modifier = Modifier.width(120.dp)
        ) {
            item.xOffset?.let {
                LabelWithValue("X:", it.toFixedDigitsString())
            }
            item.zOffset?.let {
                LabelWithValue("Z:", it.toFixedDigitsString())
            }
        }
        VerticalDivider()
        Column(
            modifier = Modifier.width(300.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Tool Info"
            )
            Text(text = item.latheTool.toString())
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = {
                onEditClicked.invoke(item)
            }) {
            Icon(Icons.Default.Edit, contentDescription = "")
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            enabled = isCurrent.not(),
            onClick = {
                onDeleteClicked.invoke(item)
            }) {
            Icon(Icons.Default.Delete, contentDescription = "")
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            enabled = isCurrent.not(),
            onClick = {
                onLoadClicked.invoke(item)
            }) {
            Icon(Icons.Default.ExitToApp, contentDescription = "")
        }
        VerticalDivider()
    }
}