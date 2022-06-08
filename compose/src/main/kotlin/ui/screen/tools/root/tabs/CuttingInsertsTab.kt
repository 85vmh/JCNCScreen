package ui.screen.tools.root.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindovercnc.linuxcnc.model.tools.CuttingInsert
import extensions.draggableScroll
import extensions.toFixedDigitsString
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.platform.VerticalScrollbar
import ui.screen.tools.root.ToolsScreenModel

private val itemModifier = Modifier.fillMaxWidth()

@Composable
fun CuttingInsertsContent(
    state: ToolsScreenModel.State,
    onEdit: (CuttingInsert) -> Unit,
    onDelete: (CuttingInsert) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            itemsIndexed(state.cuttingInserts) { index, item ->
                Surface(
                    color = gridRowColorFor(index)
                ) {
                    LatheToolView(
                        index = index,
                        item = item,
                        onEditClicked = onEdit,
                        onDeleteClicked = onDelete,
                        modifier = itemModifier
                    )
                }
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
private fun LatheToolView(
    index: Int,
    item: CuttingInsert,
    onEditClicked: (CuttingInsert) -> Unit,
    onDeleteClicked: (CuttingInsert) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.height(60.dp)
    ) {
        Text(
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center,
            text = (index + 1).toString()
        )
        VerticalDivider()
        Text(
            modifier = Modifier.width(120.dp),
            textAlign = TextAlign.Center,
            text = item.madeOf.toString()
        )
        VerticalDivider()
        Text(
            modifier = Modifier.width(80.dp),
            textAlign = TextAlign.Center,
            text = item.code
        )
        VerticalDivider()
        Text(
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center,
            text = item.tipRadius.toFixedDigitsString(1)
        )
        VerticalDivider()
        Column(
            modifier = Modifier.width(300.dp)
        ) {
            LabelWithValue("Tip Angle:", item.tipAngle.toFixedDigitsString(1))
//            LabelWithValue("Back Angle:", item.backAngle.toFixedDigitsString(1))
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
            onClick = {
                onDeleteClicked.invoke(item)
            }) {
            Icon(Icons.Default.Delete, contentDescription = "")
        }
        VerticalDivider()
    }
}