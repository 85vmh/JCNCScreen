package screen.composables.tabprograms.filesystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import extensions.draggableScroll
import usecase.model.FileSystemItem

private class ParallelogramShape(private val xOffset: Float = 10f) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            moveTo(xOffset, 0f)
            lineTo(0f, size.height)
            lineTo(size.width - xOffset, size.height)
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
        }
        return Outline.Generic(path)
    }
}

private class TrapezeShape(private val xOffset: Float = 10f) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, size.height)
            lineTo(size.width - xOffset, size.height)
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
        }
        return Outline.Generic(path)
    }
}

@Composable
fun BreadcrumbView(
    fileSystemItem: FileSystemItem,
    onFolderSelected: (String) -> Unit
) {

    val items = fileSystemItem.path
        .split("/")
        .filter { it.isNotEmpty() }

    val onClick = { text: String ->
        val clickedPath = fileSystemItem.path.substringBeforeLast(text) + text
        if (clickedPath != fileSystemItem.path) {
            onFolderSelected.invoke(clickedPath)
        }
    }

    val trapezeShape = TrapezeShape()
    val parallelogramShape = ParallelogramShape()

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier.height(36.dp).draggableScroll(scrollState, scope, Orientation.Horizontal)
    ) {
        itemsIndexed(items) { index, item ->
            BreadcrumbItem(
                shape = if (index == 0) trapezeShape else parallelogramShape,
                item = item,
                onClick = onClick
            )
        }
    }
}

@Composable
fun BreadcrumbItem(shape: Shape, item: String, onClick: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier = Modifier.fillMaxHeight()
            .clip(shape)
            .clickable(interactionSource, indication = LocalIndication.current, onClick = { onClick.invoke(item) }),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        shape = shape
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight(),
        ) {
            Text(
                text = item,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}