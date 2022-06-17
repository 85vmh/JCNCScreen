package ui.screen.tools.root.tabs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mindovercnc.model.ToolHolder
import com.mindovercnc.model.ToolHolderType
import extensions.draggableScroll
import extensions.toFixedDigitsString
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.platform.VerticalScrollbar
import ui.screen.tools.addholder.AddEditHolderScreen
import ui.screen.tools.root.ToolsScreenModel


private val itemModifier = Modifier.fillMaxWidth()

@Composable
fun ToolHoldersContent(
    state: ToolsScreenModel.State,
    onDelete: (ToolHolder) -> Unit,
    onLoad: (ToolHolder) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            itemsIndexed(state.toolHolders) { index, item ->
                HolderView(
                    item = item,
                    isCurrent = item.holderNumber == state.currentTool,
                    onEditClicked = { navigator.push(AddEditHolderScreen(it)) },
                    onDeleteClicked = onDelete,
                    onLoadClicked = onLoad,
                    modifier = itemModifier,
                    color = gridRowColorFor(index)
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
    onEditClicked: (ToolHolder) -> Unit,
    onDeleteClicked: (ToolHolder) -> Unit,
    onLoadClicked: (ToolHolder) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    val nonSelectedModifier = Modifier.height(60.dp)
    val selectedModifier = nonSelectedModifier.border(BorderStroke(1.dp, Color.Blue))

    Surface(
        modifier = modifier,
        color = color
    ) {
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
                }
            ) {
                Icon(Icons.Default.Edit, contentDescription = "")
            }
            VerticalDivider()
            IconButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                enabled = isCurrent.not(),
                onClick = {
                    onDeleteClicked.invoke(item)
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "")
            }
            VerticalDivider()
            IconButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                enabled = isCurrent.not(),
                onClick = {
                    onLoadClicked.invoke(item)
                }
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "")
            }
            VerticalDivider()
        }
    }
}

@Composable
@Preview
fun HolderViewPreview() {
    HolderView(
        ToolHolder(holderNumber = 1, type = ToolHolderType.DrillHolder),
        true,
        {},
        {},
        {}
    )
}