package extensions

import androidx.compose.foundation.gestures.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

fun Double.trimDigits(maxDigits: Int = 3): String {
    return BigDecimal(this).setScale(maxDigits, RoundingMode.HALF_EVEN).toString()
}

fun Double.stripZeros(maxDigits: Int = 3): String {
    return BigDecimal(this).setScale(maxDigits, RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()
}

@Composable
fun Modifier.draggableScroll(
    scrollState: ScrollableState,
    scope: CoroutineScope,
    orientation: Orientation = Orientation.Vertical
): Modifier {
    return draggable(rememberDraggableState { delta ->
        scope.launch {
            scrollState.scrollBy(-delta)
        }
    }, orientation = orientation)
}