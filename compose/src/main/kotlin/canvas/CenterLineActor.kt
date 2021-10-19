package canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect

class CenterLineActor(
    centerLineYCoordinate: Float,
    lineLength: Float,
    xStartPoint: Float = 0f,
) : LineActor(
    start = Offset(xStartPoint, centerLineYCoordinate),
    end = Offset(xStartPoint + lineLength, centerLineYCoordinate),
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, space, dot, space), 1f),
    thickness = thickness
) {
    companion object {
        const val thickness: Float = 0.3f
        const val dash: Float = 15f
        const val dot: Float = 5f
        const val space: Float = 15f
    }
}