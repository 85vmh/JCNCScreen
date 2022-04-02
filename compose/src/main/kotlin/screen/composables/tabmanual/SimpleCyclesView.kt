package screen.composables.tabmanual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import screen.composables.common.AppTheme
import screen.uimodel.SimpleCycle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleCyclesView(modifier: Modifier, onCycleClicked: (SimpleCycle) -> Unit) {
    Surface {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SimpleCycle.values().forEach {
                Cycle(it) {
                    onCycleClicked.invoke(it)
                }
            }
        }
    }
}

@Composable
fun Cycle(op: SimpleCycle, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (op.imgName != null) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(
                            color = AppTheme.colors.backgroundLight,
                            shape = RoundedCornerShape(6.dp),
                        ),
                    contentDescription = "",
                    bitmap = useResource(op.imgName) { loadImageBitmap(it) }
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(
                            color = AppTheme.colors.backgroundLight,
                            shape = RoundedCornerShape(6.dp),
                        )
                )
            }
            Text(
                text = op.displayableString,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(40.dp)
            )
        }
    }
}
