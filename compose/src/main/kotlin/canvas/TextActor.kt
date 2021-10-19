package canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skija.Font
import org.jetbrains.skija.Paint
import org.jetbrains.skija.TextLine
import themes.Fonts

class TextActor(
    private val text: String,
    private val offset: Offset,
    private val font: Font = Fonts.default,
    private val paint: Paint = Paint()
) : CanvasActor {
    override fun drawInto(drawScope: DrawScope) {
        drawScope.drawIntoCanvas {
            it.nativeCanvas.apply {
                val textLine = TextLine.make(text, font)
                drawTextLine(
                    textLine, offset.x, offset.y, paint
                )
            }
        }
    }
}
