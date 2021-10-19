package canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

interface CanvasActor {
    fun drawInto(drawScope: DrawScope)
}

fun CanvasActor.rotateBy(angle: Float, pivot: Offset? = null): RotateActor {
    return RotateActor(this, angle = angle, pivot = pivot)
}