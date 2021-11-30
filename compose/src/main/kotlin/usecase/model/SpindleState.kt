package usecase.model

import androidx.compose.runtime.mutableStateOf

class SpindleState(
    defaultControlMode: SpindleControlMode,
    defaultCssValue: Double,
    defaultRpmValue: Double,
) {
    val spindleType = mutableStateOf(defaultControlMode)
    val cssValue = mutableStateOf(defaultCssValue.toString())
    val rpmValue = mutableStateOf(defaultRpmValue.toString())
}