package screen.composables.tabstatus

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import extensions.draggableScroll
import extensions.toFixedDigits
import org.kodein.di.compose.rememberInstance
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.tabprograms.filesystem.BreadcrumbItem
import usecase.OffsetsUseCase
import usecase.model.OffsetEntry

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun OffsetsView(
    modifier: Modifier
) {
    val useCase: OffsetsUseCase by rememberInstance()
    val scope = rememberCoroutineScope()
    val offsets by useCase.getOffsets().collectAsState(emptyList())
    val scrollState = rememberLazyListState()

    LazyRow(
        modifier = Modifier.draggableScroll(scrollState, scope, Orientation.Horizontal)
    ) {
        items(items = offsets) {
            WorkpieceOffset(it)
        }
    }
}

@Composable
fun WorkpieceOffset(item: OffsetEntry, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier.width(130.dp).padding(horizontal = 8.dp).clickable { },
        elevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                text = item.coordinateSystem
            )
            Divider(color = Color.LightGray, thickness = 0.5.dp)
            LabelWithValue(
                label = "Z:",
                value = item.zOffset.toFixedDigits(),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
