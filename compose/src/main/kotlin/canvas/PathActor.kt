package canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import usecase.model.VisualTurningState


private const val dash = 2f
private const val space = 5f

class PathActor(
    private val visualTurningState: VisualTurningState
) : CanvasActor {
    private val traverseEffect = PathEffect.dashPathEffect(floatArrayOf(dash, space), 1f)

    override fun drawInto(drawScope: DrawScope) {
        drawScope.drawPath(path = visualTurningState.feedPath, color = Color.Black, style = Stroke(width = 1f))
        drawScope.drawPath(path = visualTurningState.traversePath, color = Color(red = 0, green = 100, blue = 0), style = Stroke(width = 1f))
    }
}