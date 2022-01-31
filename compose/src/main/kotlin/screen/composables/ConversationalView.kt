package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.common.AppTheme
import usecase.ConversationalUseCase

@Composable
fun ConversationalView(modifier: Modifier, onNewOpClicked: (ConversationalOperation) -> Unit) {
    val useCase: ConversationalUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    Surface {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ConversationalOperation.values().iterator().forEach {
                Operation(it) {
                    onNewOpClicked.invoke(it)
                }
            }
        }
    }
}

enum class ConversationalOperation(val displayableString: String, val imgName: String? = null) {
    OdTurning("OD Turning", "od.png"),
    IdTurning("ID Turning", "id.png"),
    Profiling("Profiling", "lathe-depth-step.png"),
    Facing("Facing", "facing.png"),
    Grooving("Grooving\nParting", "parting.png"),
    Threading("Threading", "threading.png"),
    Drilling("Drilling\nReaming"),
    KeySlot("Slotting", "slotting.png"),
}

@Composable
fun Operation(op: ConversationalOperation, clicked: () -> Unit) {
    Card(
        modifier = Modifier.clickable {
            clicked.invoke()
        },
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
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
