package usecase.model

import androidx.compose.ui.graphics.Path
import kotlin.math.abs

data class ProgramData(
    var feedPath: Path = Path(),
    var traversePath: Path = Path(),
    var tracePath: Path = Path(),
) {
    data class ProgramSize(
        val width: Float,
        val height: Float
    )

    val programSize
        get() = ProgramSize(
            width = with(feedPath.getBounds()) {
                abs(left) + abs(right)
            },
            height = feedPath.getBounds().bottom //we consider it from center line which is always 0
        )

    val xAxisLength get() = feedPath.getBounds().bottom

    val zAxisLength get() = feedPath.getBounds().right
}
