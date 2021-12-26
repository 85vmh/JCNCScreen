package screen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import extensions.trimDigits
import org.kodein.di.compose.rememberInstance
import usecase.OffsetsUseCase
import usecase.model.OffsetEntry

@Composable
fun OffsetsView() {
    val useCase: OffsetsUseCase by rememberInstance()
    val scope = rememberCoroutineScope()
    val offsets by useCase.getOffsets().collectAsState(emptyList())

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        OffsetsList(offsets)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OffsetsList(toolsList: List<OffsetEntry>) {
    LazyColumn(
        modifier = Modifier

    ) {
        stickyHeader {
            OffsetsHeader()
            Divider(color = Color.Gray, thickness = 0.5.dp)
        }
        items(toolsList) { item ->
            OffsetRow(item)
            Divider(color = Color.Gray, thickness = 0.5.dp)
        }
    }
}

@Composable
fun OffsetsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.width(80.dp),
            text = "Coordinate System"
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
fun OffsetRow(item: OffsetEntry, modifier: Modifier = Modifier) {
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
            text = item.coordinateSystem
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
