package ui.screen.manual.root

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import extensions.draggableScroll
import screen.uimodel.SimpleCycle

@Composable
fun DrawerContent(
    onCycleSelected: (SimpleCycle) -> Unit
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Simple Cycles",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            items(SimpleCycle.values()) { item ->
                Cycle(item, Modifier.padding(vertical = 8.dp)) {
                    onCycleSelected.invoke(item)
                }
            }
        }
    }
}

@Composable
fun Cycle(op: SimpleCycle, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(8.dp)
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(interactionSource, indication = LocalIndication.current, onClick = onClick),
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
        ) {

            val imageSize = 60.dp
            if (op.imgName != null) {
                Image(
                    modifier = Modifier
                        .size(imageSize)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(6.dp),
                        ),
                    contentDescription = "",
                    bitmap = useResource(op.imgName) { loadImageBitmap(it) }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(6.dp),
                        )
                )
            }
            Text(
                text = op.displayableString,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
