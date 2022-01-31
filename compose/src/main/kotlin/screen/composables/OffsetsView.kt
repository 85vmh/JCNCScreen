package screen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import extensions.trimDigits
import org.kodein.di.compose.rememberInstance
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

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier

        ) {
            stickyHeader {
                OffsetsHeader()
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
            items(offsets) { item ->
                OffsetRow(item)
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun OffsetsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.width(70.dp),
            textAlign = TextAlign.Center,
            text = "WCS"
        )
        VerticalDivider()
        Text(
            modifier = Modifier.width(130.dp),
            textAlign = TextAlign.Center,
            text = "Offset"
        )
        VerticalDivider()
    }
}

@Composable
fun OffsetRow(item: OffsetEntry, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(60.dp)
            .clickable {

            }
    ) {
        Text(
            modifier = Modifier.width(70.dp),
            textAlign = TextAlign.Center,
            text = item.coordinateSystem
        )
        VerticalDivider()
        Column(
            modifier = Modifier.width(130.dp),
        ) {
            LabelWithValue("X:", item.xOffset.trimDigits())
            LabelWithValue("Z:", item.zOffset.trimDigits())
        }
        VerticalDivider()
    }
}
