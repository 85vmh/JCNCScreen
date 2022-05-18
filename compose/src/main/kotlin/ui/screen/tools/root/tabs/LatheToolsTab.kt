package ui.screen.tools.root.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.mindovercnc.base.data.tools.CuttingInsert
import com.mindovercnc.base.data.tools.LatheTool
import extensions.draggableScroll
import extensions.toFixedDigitsString
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.platform.VerticalScrollbar
import ui.screen.tools.root.ToolsScreenModel

@Composable
fun LatheToolsContent(screenModel: ToolsScreenModel) {
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
            itemsIndexed(state.latheTools) { index, item ->
                when (item) {
                    is LatheTool.Turning -> TurningToolView(
                        index = index,
                        item = item,
                        onEditClicked = {},
                        onDeleteClicked = {}
                    )
                    is LatheTool.Boring -> BoringToolView(
                        index = index,
                        item = item,
                        onEditClicked = {},
                        onDeleteClicked = {}
                    )
                    is LatheTool.DrillingReaming -> DrillingReamingToolView(
                        index = index,
                        item = item,
                        onEditClicked = {},
                        onDeleteClicked = {}
                    )
                    is LatheTool.Parting -> PartingToolView(
                        index = index,
                        item = item,
                        onEditClicked = {},
                        onDeleteClicked = {}
                    )
                    else -> {
                        Text("Not implemented: $item")
                    }
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
private fun TurningToolView(
    index: Int,
    item: LatheTool.Turning,
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
        Column(
            modifier = Modifier.width(180.dp)
        ) {
            with(item.insert) {
                LabelWithValue("Material:", this.madeOf.name)
                LabelWithValue("Code:", this.code)
                LabelWithValue("Tip Radius:", this.tipRadius.toFixedDigitsString(1))
            }
        }
        VerticalDivider()
        Text(
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center,
            text = "Tool Type: Turning"
        )
        VerticalDivider()
        LabelWithValue("Orientation:", item.tipOrientation.name, modifier = Modifier.width(150.dp))
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
        Column(
            modifier = Modifier.width(180.dp)
        ) {
            with(item.insert) {
                LabelWithValue("Material:", this.madeOf.name)
                LabelWithValue("Code:", this.code)
                LabelWithValue("Tip Radius:", this.tipRadius.toFixedDigitsString(1))
            }
        }
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