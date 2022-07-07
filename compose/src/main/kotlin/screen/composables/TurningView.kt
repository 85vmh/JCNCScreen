package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import canvas.*
import usecase.model.VisualTurningState


@Preview
@Composable
fun VisualTurningPreview() {
    VisualTurning(
        VisualTurningState(

        )
    )
}

@Composable
fun VisualTurning(
    state: VisualTurningState,
    modifier: Modifier = Modifier
) {

    val xAxisColor = Color(0xFF14B86F)
    val zAxisColor = Color(0xFF4282E6)

    Canvas(modifier.fillMaxSize()) {
        val actors = mutableListOf<CanvasActor>()

        actors.add(
            CenterLineActor(lineLength = size.width)
                .translateTo(Offset(0f, state.translate.y))
        )

        println("--Pixel per unit: ${state.pixelPerUnit}")

        actors.add(
            RulerActor(
                placement = RulerActor.Placement.Top,
                pixelPerUnit = state.pixelPerUnit,
                start = Offset(0f, 0f),
                minValue = state.wcsLimits.zMin,
                maxValue = state.wcsLimits.zMax,
                lineColor = zAxisColor
            ).translateTo(Offset(state.translate.x, 0f))
        )
        actors.add(
            RulerActor(
                placement = RulerActor.Placement.Bottom,
                pixelPerUnit = state.pixelPerUnit,
                start = Offset(0f, this.size.height),
                minValue = state.wcsLimits.zMin,
                maxValue = state.wcsLimits.zMax,
                lineColor = zAxisColor
            ).translateTo(Offset(state.translate.x, 0f))
        )
        actors.add(
            RulerActor(
                placement = RulerActor.Placement.Left,
                pixelPerUnit = state.pixelPerUnit,
                start = Offset(0f, 0f),
                minValue = state.wcsLimits.xMin,
                maxValue = state.wcsLimits.xMax,
                diameterMode = true,
                lineColor = xAxisColor
            ).translateTo(Offset(0f, state.translate.y))
        )
        actors.add(
            RulerActor(
                placement = RulerActor.Placement.Right,
                pixelPerUnit = state.pixelPerUnit,
                start = Offset(this.size.width, 0f),
                minValue = state.wcsLimits.xMin,
                maxValue = state.wcsLimits.xMax,
                diameterMode = true,
                lineColor = xAxisColor
            ).translateTo(Offset(0f, state.translate.y))
        )

        actors.add(
            PathActor(state.programData)
                .translateTo(state.translate)
            //.translateTo(state.wcsPosition.toOffset(state.pixelPerUnit))
        )

        actors.add(
            ReferenceActor(
                radius = 10f,
                text = state.currentWcs
            )
                .translateTo(state.translate)
            //.translateTo(state.wcsPosition.toOffset(state.pixelPerUnit))
        )
        actors.add(
            AxesActor(
                xAxisLength = state.xAxisLength,
                zAxisLength = state.zAxisLength,
                xColor = xAxisColor,
                zColor = zAxisColor
            )
                .translateTo(state.translate)
            //.translateTo(state.wcsPosition.toOffset(state.pixelPerUnit))
        )

//        actors.add(
//            ToolTraceActor(state).translateTo(state.translate)
//        )

        val insertShape = InsertShape.Rhomb(
            angle = 55,
            height = state.pixelPerUnit * 7f
        )

        actors.add(
            ToolActor(
                insertShape = insertShape
            )
                .translateTo(state.translate)
                .rotateBy(30f, state.translate)
                .translateTo(state.toolPosition.minus(state.wcsPosition).toOffset(state.pixelPerUnit))
        )

        clipRect {
            for (anActor in actors) {
                anActor.drawInto(this)
            }
        }
    }
}