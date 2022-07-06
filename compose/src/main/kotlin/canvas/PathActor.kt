package canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import usecase.model.ProgramData
import usecase.model.VisualTurningState

private const val dash = 2f
private const val space = 5f

class PathActor(
    private val programData: ProgramData,
    private val feedPathColor: Color = Color.Black,
    private val traversePathColor: Color = Color(red = 0, green = 100, blue = 0),
    private val feedThickness: Float = 1f,
    private val traverseThickness: Float = 0.3f,
) : CanvasActor {
    private val traverseEffect = PathEffect.dashPathEffect(floatArrayOf(dash, space), 1f)

    override fun drawInto(drawScope: DrawScope) {
        drawScope.drawPath(
            path = programData.feedPath,
            color = feedPathColor,
            style = Stroke(width = feedThickness, cap = StrokeCap.Round)
        )
        drawScope.drawPath(
            path = programData.traversePath,
            color = traversePathColor,
            style = Stroke(width = traverseThickness, cap = StrokeCap.Round)
        )
    }
}