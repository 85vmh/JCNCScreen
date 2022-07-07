package usecase.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.mindovercnc.model.MachineLimits
import com.mindovercnc.model.PathElement
import com.mindovercnc.model.Point2D
import com.mindovercnc.model.WcsLimits

const val extraAxisLength = 20 //add another 30 px for the tip of the arrow to exit the path

data class VisualTurningState(
    val machineLimits: MachineLimits = MachineLimits(),
    val currentWcs: String = "",
    val wcsPosition: Point2D = Point2D.zero,
    val toolPosition: Point2D = Point2D.zero,
    val defaultPixelsPerUnit: Float = 1f, //this will hold the pixel per unit needed so that the programs fits within the view bounds.
    val scale: Float = 1f,
    val translate: Offset = Offset.Zero,
    val pathElements: List<PathElement> = emptyList(),
    val programData: ProgramData = ProgramData(),
    val viewportSize: IntSize = IntSize.Zero
) {
    val pixelPerUnit: Float
        get() {
            return defaultPixelsPerUnit * scale
        }

    val wcsLimits: WcsLimits
        get() {
            return machineLimits.toWcsLimits(wcsPosition)
        }

    val xAxisLength: Float
        get() = programData.xAxisLength + extraAxisLength

    val zAxisLength: Float
        get() = programData.zAxisLength + extraAxisLength
}
