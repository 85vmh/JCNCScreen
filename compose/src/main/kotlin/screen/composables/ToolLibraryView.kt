package screen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
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

        val longerList = mutableListOf<LatheTool>()
        longerList.addAll(toolsList)
        longerList.addAll(toolsList)
        longerList.addAll(toolsList)
        longerList.addAll(toolsList)
        ToolsList(longerList)

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

@Composable
fun ToolsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.width(80.dp),
            text = "Tool No"
        )
        Text(
            modifier = Modifier.width(100.dp),
            text = "X Offset"
        )
        Text(
            modifier = Modifier.width(100.dp),
            text = "Z Offset"
        )
    }
}

@Composable
fun ToolRow(item: LatheTool, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {

            }
    ) {
        Text(
            modifier = Modifier.width(80.dp),
            text = item.toolNo.toString()
        )
        Text(
            modifier = Modifier.width(100.dp),
            text = item.xOffset.trimDigits()
        )
        Text(
            modifier = Modifier.width(100.dp),
            text = item.zOffset.trimDigits()
        )
    }
}
