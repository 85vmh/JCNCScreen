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
import com.mindovercnc.model.CuttingInsert
import com.mindovercnc.model.LatheTool
import extensions.draggableScroll
import extensions.toFixedDigitsString
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.platform.VerticalScrollbar
import ui.screen.tools.root.ToolsScreenModel

private val itemModifier = Modifier.fillMaxWidth()

@Composable
fun LatheToolsContent(
    state: ToolsScreenModel.State,
    modifier: Modifier = Modifier
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
            itemsIndexed(state.latheTools) { index, item ->
                Surface(
                    color = gridRowColorFor(index)
                ) {
                    when (item) {
                        is LatheTool.Turning -> TurningToolView(
                            index = index,
                            item = item,
                            onEditClicked = {},
                            onDeleteClicked = {},
                            modifier = itemModifier
                        )
                        is LatheTool.Boring -> BoringToolView(
                            index = index,
                            item = item,
                            onEditClicked = {},
                            onDeleteClicked = {},
                            modifier = itemModifier
                        )
                        is LatheTool.DrillingReaming -> DrillingReamingToolView(
                            index = index,
                            item = item,
                            onEditClicked = {},
                            onDeleteClicked = {},
                            modifier = itemModifier
                        )
                        is LatheTool.Parting -> PartingToolView(
                            index = index,
                            item = item,
                            onEditClicked = {},
                            onDeleteClicked = {},
                            modifier = itemModifier
                        )
                        else -> {
                            Text("Not implemented: $item")
                        }
                    }
                }
                HorizontalDivider()
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
private fun TurningToolView(
    index: Int,
    item: LatheTool.Turning,
    onEditClicked: (LatheTool) -> Unit,
    onDeleteClicked: (LatheTool) -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.height(60.dp)
    ) {
        Text(
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            text = (index + 1).toString()
        )
        VerticalDivider()
        MaterialDetails(
            insert = item.insert,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider()
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = "Tool Type: Turning"
        )
        VerticalDivider()
        LabelWithValue("Orientation:", item.tipOrientation.name, modifier = Modifier.width(150.dp))
        VerticalDivider()
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = item.spindleDirection.name
        )
        VerticalDivider()
        Column(
            modifier = Modifier.weight(1f)
        ) {
            LabelWithValue("Minutes used:", item.minutesUsed.toFixedDigitsString(1))
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

@Composable
private fun BoringToolView(
    index: Int,
    item: LatheTool.Boring,
    modifier: Modifier = Modifier,
    onEditClicked: (LatheTool) -> Unit,
    onDeleteClicked: (LatheTool) -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.height(60.dp)
    ) {
        Text(
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            text = (index + 1).toString()
        )
        VerticalDivider()
        MaterialDetails(
            insert = item.insert,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider()
        Text(
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center,
            text = "Tool Type: Boring"
        )
        VerticalDivider()
        LabelWithValue("Orientation:", item.tipOrientation.name, modifier = Modifier.width(150.dp))
        VerticalDivider()
        Column(
            modifier = Modifier.width(150.dp)
        ) {
            LabelWithValue("Minimum Diameter:", item.minBoreDiameter.toFixedDigitsString(1))
            LabelWithValue("Maximum Z Depth:", item.maxZDepth.toFixedDigitsString(1))
        }
        VerticalDivider()
        Text(
            modifier = Modifier.width(80.dp),
            textAlign = TextAlign.Center,
            text = item.spindleDirection.name
        )
        VerticalDivider()
        Column(
            modifier = Modifier.width(100.dp)
        ) {
            LabelWithValue("Minutes used:", item.minutesUsed.toFixedDigitsString(1))
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

@Composable
private fun DrillingReamingToolView(
    index: Int,
    item: LatheTool.DrillingReaming,
    modifier: Modifier = Modifier,
    onEditClicked: (LatheTool) -> Unit,
    onDeleteClicked: (LatheTool) -> Unit,
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
        LabelWithValue("Orientation:", item.tipOrientation.name)
        VerticalDivider()
        Column {
            LabelWithValue("Minimum Diameter:", item.toolDiameter.toFixedDigitsString(1))
            LabelWithValue("Maximum Z Depth:", item.maxZDepth.toFixedDigitsString(1))
        }
        VerticalDivider()
        Text(
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center,
            text = item.spindleDirection.name
        )
        VerticalDivider()
        Column(
            modifier = Modifier.width(300.dp)
        ) {
            LabelWithValue("Minutes used:", item.minutesUsed.toFixedDigitsString(1))
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

@Composable
private fun PartingToolView(
    index: Int,
    item: LatheTool.Parting,
    modifier: Modifier = Modifier,
    onEditClicked: (LatheTool) -> Unit,
    onDeleteClicked: (LatheTool) -> Unit,
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
        Column(
            modifier = Modifier.width(300.dp)
        ) {
            with(item.insert) {
                LabelWithValue("Material:", this.madeOf.name)
                LabelWithValue("Code:", this.code)
                LabelWithValue("Tip Radius:", this.tipRadius.toFixedDigitsString(1))
            }
        }
        VerticalDivider()
        LabelWithValue("Orientation:", item.tipOrientation.name)
        VerticalDivider()
        Column {
            LabelWithValue("Blade Width:", item.bladeWidth.toFixedDigitsString(1))
            LabelWithValue("Maximum X Depth:", item.maxXDepth.toFixedDigitsString(1))
        }
        VerticalDivider()
        Text(
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center,
            text = item.spindleDirection.name
        )
        VerticalDivider()
        Column(
            modifier = Modifier.width(300.dp)
        ) {
            LabelWithValue("Minutes used:", item.minutesUsed.toFixedDigitsString(1))
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

@Composable
fun MaterialDetails(
    insert: CuttingInsert,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        LabelWithValue(
            label = "Material:",
            value = insert.madeOf.name,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        LabelWithValue(
            label = "Code:",
            value = insert.code,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        LabelWithValue(
            "Tip Radius:",
            insert.tipRadius.toFixedDigitsString(1),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HorizontalDivider() {
    Divider(
        color = Color.LightGray,
        thickness = 0.5.dp
    )
}
