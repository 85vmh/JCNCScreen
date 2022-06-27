package usecase.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

data class VisualTurningState(
    val feedPath: Path = Path(),
    val traversePath: Path = Path(),
    val scale: Float = 1f,
    val translate: Offset = Offset(500f, 80f)
)
