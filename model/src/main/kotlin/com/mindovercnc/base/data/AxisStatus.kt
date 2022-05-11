package com.mindovercnc.base.data

data class AxisStatus(
    val minPositionLimit: Double,
    val maxPositionLimit: Double,
    val currentVelocity: Double
)