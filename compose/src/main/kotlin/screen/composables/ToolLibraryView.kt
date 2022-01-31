package screen.composables

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
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
import com.mindovercnc.base.data.LatheTool
import extensions.draggableScroll
import extensions.trimDigits
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.kodein.di.compose.rememberInstance
import screen.composables.platform.VerticalScrollbar
import usecase.ToolsUseCase
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolLibraryView(
    modifier: Modifier
) {
    val useCase: ToolsUseCase by rememberInstance()
    val scope = rememberCoroutineScope()
    val toolsList by useCase.getTools().collectAsState(emptyList())

    Box(
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            stickyHeader {
                ToolsHeader()
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
            items(toolsList) { item ->
                ToolRow(item)
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd).width(30.dp),
            scrollState,
            toolsList.size,
            60.dp
        )
    }
}

private enum class ToolsColumnModifier(val modifier: Modifier) {
    ToolNo(Modifier.width(50.dp)),
    Offset(Modifier.width(130.dp)),
    Wear(Modifier.width(130.dp)),
    TipRadius(Modifier.width(100.dp)),
    Orientation(Modifier.width(110.dp)),
}

@Composable
fun ToolsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = ToolsColumnModifier.ToolNo.modifier,
            textAlign = TextAlign.Center,
            text = "Tool"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Offset.modifier,
            textAlign = TextAlign.Center,
            text = "Offset"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Wear.modifier,
            textAlign = TextAlign.Center,
            text = "Wear"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.TipRadius.modifier,
            textAlign = TextAlign.Center,
            text = "Tip Radius"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Orientation.modifier,
            textAlign = TextAlign.Center,
            text = "Orientation"
        )
        VerticalDivider()
    }
}

@Composable
fun ToolRow(item: LatheTool, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(60.dp)
            .clickable {

            }
    ) {
        Text(
            modifier = ToolsColumnModifier.ToolNo.modifier,
            textAlign = TextAlign.Center,
            text = item.toolNo.toString()
        )
        VerticalDivider()
        Column(
            modifier = ToolsColumnModifier.Offset.modifier
        ) {
            LabelWithValue("X:", item.xOffset.trimDigits())
            LabelWithValue("Z:", item.zOffset.trimDigits())
        }
        VerticalDivider()
        Column(
            modifier = ToolsColumnModifier.Offset.modifier
        ) {
            LabelWithValue("X:", item.xWear.trimDigits())
            LabelWithValue("Z:", item.zWear.trimDigits())
        }
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.TipRadius.modifier,
            textAlign = TextAlign.Center,
            text = item.tipRadius.trimDigits()
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Orientation.modifier,
            textAlign = TextAlign.Center,
            text = item.orientation.angle.toString()
        )
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = { }) {
            Icon(Icons.Default.Edit, contentDescription = "")
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = { }) {
            Icon(Icons.Default.Delete, contentDescription = "")
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = { }) {
            Icon(Icons.Default.ExitToApp, contentDescription = "")
        }
        VerticalDivider()
    }
}

class DefaultFlingBehavior(
    private val flingDecay: DecayAnimationSpec<Float>
) : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        // come up with the better threshold, but we need it since spline curve gives us NaNs
        return if (abs(initialVelocity) > 1f) {
            var velocityLeft = initialVelocity
            var lastValue = 0f
            AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocity,
            ).animateDecay(flingDecay) {
                val delta = value - lastValue
                val consumed = scrollBy(delta)
                lastValue = value
                velocityLeft = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
            velocityLeft
        } else {
            initialVelocity
        }
    }
}

