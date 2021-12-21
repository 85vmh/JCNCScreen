package screen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.data.LatheTool
import extensions.trimDigits
import org.kodein.di.compose.rememberInstance
import usecase.ToolsUseCase

@Composable
fun ToolLibraryView(onFinish: () -> Unit) {
    val useCase: ToolsUseCase by rememberInstance()
    val scope = rememberCoroutineScope()
    val toolsList by useCase.getTools().collectAsState(emptyList())

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        ToolsList(toolsList)

        Button(onClick = {
            onFinish.invoke()
        }) {
            Text("<- Back")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolsList(toolsList: List<LatheTool>) {
    LazyColumn(
        modifier = Modifier

    ) {
        stickyHeader {
            ToolsHeader()
            Divider(color = Color.Gray, thickness = 0.5.dp)
        }
        items(toolsList) { item ->
            ToolRow(item)
            Divider(color = Color.Gray, thickness = 0.5.dp)
        }
    }
}

enum class ColumnModifier(val modifier: Modifier) {
    ToolNo(Modifier.width(60.dp)),
    XOffs(Modifier.width(80.dp)),
    ZOffs(Modifier.width(80.dp)),
    XWear(Modifier.width(80.dp)),
    ZWear(Modifier.width(80.dp)),
    TipRadius(Modifier.width(100.dp)),
    Orientation(Modifier.width(110.dp)),
}

@Composable
fun ToolsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = ColumnModifier.ToolNo.modifier,
            textAlign = TextAlign.Center,
            text = "Tool #"
        )
        Text(
            modifier = ColumnModifier.XOffs.modifier,
            textAlign = TextAlign.Right,
            text = "X"
        )
        Text(
            modifier = ColumnModifier.ZOffs.modifier,
            textAlign = TextAlign.Right,
            text = "Z"
        )
        Text(
            modifier = ColumnModifier.XWear.modifier,
            textAlign = TextAlign.Right,
            text = "X Wear"
        )
        Text(
            modifier = ColumnModifier.ZWear.modifier,
            textAlign = TextAlign.Right,
            text = "Z Wear"
        )
        Text(
            modifier = ColumnModifier.TipRadius.modifier,
            textAlign = TextAlign.Right,
            text = "Tip Radius"
        )
        Text(
            modifier = ColumnModifier.Orientation.modifier,
            textAlign = TextAlign.Right,
            text = "Orientation"
        )
    }
}

@Composable
fun ToolRow(item: LatheTool, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {

            }
    ) {
        Text(
            modifier = ColumnModifier.ToolNo.modifier,
            textAlign = TextAlign.Center,
            text = item.toolNo.toString()
        )
        Text(
            modifier = ColumnModifier.XOffs.modifier,
            textAlign = TextAlign.Right,
            text = item.xOffset.trimDigits()
        )
        Text(
            modifier = ColumnModifier.ZOffs.modifier,
            textAlign = TextAlign.Right,
            text = item.zOffset.trimDigits()
        )
        Text(
            modifier = ColumnModifier.XWear.modifier,
            textAlign = TextAlign.Right,
            text = item.xWear.trimDigits()
        )
        Text(
            modifier = ColumnModifier.ZWear.modifier,
            textAlign = TextAlign.Right,
            text = item.zWear.trimDigits()
        )
        Text(
            modifier = ColumnModifier.TipRadius.modifier,
            textAlign = TextAlign.Right,
            text = item.tipRadius.trimDigits()
        )
        Text(
            modifier = ColumnModifier.Orientation.modifier,
            textAlign = TextAlign.Center,
            text = item.orientation.angle.toString()
        )
    }
}
