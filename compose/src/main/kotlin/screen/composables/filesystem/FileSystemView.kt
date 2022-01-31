package screen.composables.filesystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.draggableScroll
import kotlinx.coroutines.runBlocking
import screen.composables.platform.VerticalScrollbar
import usecase.model.FileSystemItem

@Composable
fun FileSystemView(
    fileSystemItem: FileSystemItem
) = Surface(
    modifier = Modifier.fillMaxSize()
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column {
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        Row {
            Box {
                LazyColumn(
                    modifier = Modifier.draggableScroll(scrollState, scope),
                    state = scrollState
                ) {
                    items(fileSystemItem.children) { item ->
                        FileSystemItemView(item)
                        Divider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
                VerticalScrollbar(
                    Modifier.align(Alignment.CenterEnd),
                    scrollState,
                    fileSystemItem.children.size,
                    60.dp
                )
            }
        }
    }
}

@Composable
private fun FileSystemItemView(item: FileSystemItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .clickable {
                item.clicked.invoke()
            }
            .padding(start = 8.dp)
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val resourcePath = when {
            item.isDirectory -> "folder-icon.png"
            else -> "gcode.png"
        }
        Image(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            contentDescription = "",
            bitmap = useResource(resourcePath) { loadImageBitmap(it) }
        )
        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                textAlign = TextAlign.Left,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                text = item.name
            )
            Text(
                textAlign = TextAlign.Left,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                text = item.lastModified.toString()
            )
        }
    }
}