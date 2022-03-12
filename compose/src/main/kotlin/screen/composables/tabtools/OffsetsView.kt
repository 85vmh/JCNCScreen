package screen.composables.tabtools

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import extensions.toFixedDigitsString
import org.kodein.di.compose.rememberInstance
import screen.composables.LabelWithValue
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

    val currentWcs by useCase.currentWcs.collectAsState("--")

    LazyRow(
        modifier = Modifier.draggableScroll(scrollState, scope, Orientation.Horizontal)
    ) {
        items(items = offsets) {
            WorkpieceOffset(
                item = it,
                isActive = currentWcs == it.coordinateSystem,
                modifier = modifier
            ) {
                useCase.setActiveOffset(it.coordinateSystem)
            }
        }
    }
}

@Composable
fun WorkpieceOffset(item: OffsetEntry, isActive: Boolean, modifier: Modifier = Modifier, wcsSelected: () -> Unit) {
    Card(
        backgroundColor = when {
            isActive -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        },
        modifier = Modifier
            .width(150.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                wcsSelected.invoke()
            },
        elevation = 16.dp
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
                label = "X:",
                value = item.xOffset.toFixedDigitsString(),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LabelWithValue(
                label = "Z:",
                value = item.zOffset.toFixedDigitsString(),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
