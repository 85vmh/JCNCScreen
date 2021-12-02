package usecase.model

import androidx.compose.runtime.mutableStateOf

class SpindleState(
    defaultControlMode: SpindleControlMode,
    defaultRpmValue: Int,
    defaultCssValue: Int,
    defaultMaxCssRpm: Int,
    orientedStop: Boolean,
    defaultStopAngle: Double,
) {
    val spindleMode = mutableStateOf(defaultControlMode)
    val cssValue = mutableStateOf(defaultCssValue.toString())
    val rpmValue = mutableStateOf(defaultRpmValue.toString())
    val maxCssRpm = mutableStateOf(defaultMaxCssRpm.toString())
    val orientedStop = mutableStateOf(orientedStop)
    val stopAngle = mutableStateOf(defaultStopAngle.toString())
}