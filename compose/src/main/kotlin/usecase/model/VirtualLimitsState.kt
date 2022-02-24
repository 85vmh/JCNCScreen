package usecase.model

import androidx.compose.runtime.mutableStateOf

class VirtualLimitsState(
    xMinus: Double,
    xPlus: Double,
    zMinus: Double,
    zPlus: Double,
    xMinusActive: Boolean,
    xPlusActive: Boolean,
    zMinusActive: Boolean,
    zPlusActive: Boolean
) {
    val xMinus = mutableStateOf(xMinus)
    val xPlus = mutableStateOf(xPlus)
    val zMinus = mutableStateOf(zMinus)
    val zPlus = mutableStateOf(zPlus)
    val xMinusActive = mutableStateOf(xMinusActive)
    val xPlusActive = mutableStateOf(xPlusActive)
    val zMinusActive = mutableStateOf(zMinusActive)
    val zPlusActive = mutableStateOf(zPlusActive)
}