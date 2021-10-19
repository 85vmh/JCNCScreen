package di

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import canvas.*


@Preview
@Composable
fun VisualTurningPreview() {
    VisualTurning()
}

@Composable
internal fun VisualTurning(
    modifier: Modifier = Modifier
) {

    Canvas(modifier.fillMaxSize()) {

//        val tipOffset = Offset(220f, 200f)
//        ArrowTipActor(tipOffset = tipOffset, height = 50f, color = Color.Black).drawInto(this)
//        ArrowTipActor(tipOffset = tipOffset, height = 50f, color = Color.Blue).rotateBy(90f, tipOffset).drawInto(this)
//        ArrowTipActor(tipOffset = tipOffset, height = 50f, color = Color.Red).rotateBy(180f, tipOffset).drawInto(this)
//        ArrowTipActor(tipOffset = tipOffset, height = 50f, color = Color.Green).rotateBy(270f, tipOffset).drawInto(this)

        val centerLine = size.center.y
        val actors = listOf(
            CenterLineActor(centerLine, size.width),
            StockContourActor(centerLineYCoordinate = centerLine, stockDiameter = 450f, stockLength = 600f),
            //TextActor("Cucu bau", Offset(x = 100f, y = 200f)),
            CylinderActor(Offset(x = 0f, y = centerLine), 200f, 180f),
            DividerActor(Offset(x = 180f, y = centerLine), 200f, 180f),
            CylinderActor(Offset(x = 180f, y = centerLine), 180f, 180f),
            DividerActor(Offset(x = 360f, y = centerLine), 180f, 180f),
            ChamferActor(Offset(x = 360f, y = centerLine), 20f, 180f, 170f),
            StartDividerActor(Offset(x = 380f, y = centerLine), 170f)
        )
        for (anActor in actors) {
            anActor.drawInto(this)
        }
    }
}

sealed class CylinderFeature {
    data class Chamfer(val width: Float) : CylinderFeature()
    data class Radius(val radius: Float, val centerPoint: Offset) : CylinderFeature()
}