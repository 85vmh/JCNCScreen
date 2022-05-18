package ui.screen.manual.root

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import extensions.draggableScroll
import extensions.toFixedDigitsString
import screen.composables.LabelWithValue

val offsetItemModifier = Modifier
    .width(300.dp)
    .padding(horizontal = 8.dp, vertical = 4.dp)

@Composable
fun WcsOffsetsView(
    wcs: WcsUiModel,
    onOffsetClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = offsetItemModifier
) {
    val scope = rememberCoroutineScope()
    //val scrollState = rememberLazyListState()
    val scrollState = rememberLazyGridState()

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        state = scrollState,
        modifier = modifier.draggableScroll(scrollState, scope, Orientation.Horizontal)
    ) {
        items(wcs.wcsOffsets) {
            WorkpieceOffset(
                item = it,
                isActive = wcs.activeOffset == it.coordinateSystem,
                modifier = itemModifier,
                wcsSelected = { onOffsetClick(it.coordinateSystem) }
            )
        }
    }

//    LazyRow(
//        state = scrollState,
//        modifier = modifier.draggableScroll(scrollState, scope, Orientation.Horizontal)
//    ) {
//        items(items = wcs.wcsOffsets) {
//            WorkpieceOffset(
//                item = it,
//                isActive = wcs.activeOffset == it.coordinateSystem,
//                modifier = itemModifier,
//                wcsSelected = { onOffsetClick(it.coordinateSystem) }
//            )
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkpieceOffset(
    item: WcsOffset,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    wcsSelected: () -> Unit
) {
    Surface(
        color = when {
            isActive -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        },
        modifier = modifier,
        shape = shape,
        shadowElevation = 16.dp,
        onClick = { wcsSelected.invoke() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                text = item.coordinateSystem
            )
            Divider(color = Color.LightGray, thickness = 0.5.dp)
            LabelWithValue(
                modifier = Modifier.padding(top = 8.dp),
                paddingStart = 0.dp,
                paddingEnd = 0.dp,
                label = "X offset:",
                value = item.xOffset.toFixedDigitsString(),
            )
            Spacer(modifier = Modifier.size(8.dp))
            LabelWithValue(
                modifier = Modifier.padding(bottom = 8.dp),
                paddingStart = 0.dp,
                paddingEnd = 0.dp,
                label = "Z offset:",
                value = item.zOffset.toFixedDigitsString(),
            )
        }
    }
}
