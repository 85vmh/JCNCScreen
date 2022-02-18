package usecase.model

import androidx.compose.runtime.mutableStateOf

class TaperState(
    taperAngle: Double,
) {
    val taperAngle = mutableStateOf(taperAngle)
}