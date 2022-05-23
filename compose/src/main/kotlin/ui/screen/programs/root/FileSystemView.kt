package ui.screen.programs.root

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
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
import screen.composables.platform.VerticalScrollbar
import usecase.model.FileSystemItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FileSystemView(
    fileSystemItem: FileSystemItem.FolderItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        Column {
            Divider(
                color = Color.LightGray,
                thickness = 0.5.dp
            )
            Row {
                Box {
                    LazyColumn(
                        modifier = Modifier.draggableScroll(scrollState, scope),
                        state = scrollState
                    ) {
                        items(fileSystemItem.children) { item ->
                            FileSystemItemView(item)
                            Divider(
                                color = Color.LightGray,
                                thickness = 0.5.dp
                            )
                        }
                    }
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        scrollState = scrollState,
                        itemCount = fileSystemItem.children.size,
                        averageItemSize = 60.dp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileSystemItemView(
    item: FileSystemItem,
    modifier: Modifier = Modifier
) {
    val color = when(item){
        is FileSystemItem.FileItem -> MaterialTheme.colorScheme.surfaceVariant
        is FileSystemItem.FolderItem -> MaterialTheme.colorScheme.tertiaryContainer
    }

    Surface(
        color = color,
        onClick = {
            when (item) {
                is FileSystemItem.FolderItem -> item.clicked.invoke()
                is FileSystemItem.FileItem -> item.clicked.invoke()
            }
        }
    ) {
        Row(
            modifier = modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            val resourcePath = when (item) {
                is FileSystemItem.FolderItem -> "folder-icon.png"
                is FileSystemItem.FileItem -> "gcode.png"
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
                    text = millisToLastModified(item.lastModified)
                )
            }
        }
    }
}

private fun millisToLastModified(millis: Long): String {
//    when{
//        DateUtils.isToday(long timeInMilliseconds)
//    }


    return SimpleDateFormat("dd/MM/yyyy").format(Date(millis))
}
