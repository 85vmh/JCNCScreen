package com.mindovercnc.base.data

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

fun CncStatus.isDiameterMode() = taskStatus.activeCodes.gCodes.contains(7.0f)

fun CncStatus.getG53Position() = motionStatus.trajectoryStatus.currentActualPosition

fun CncStatus.getDisplayablePosition(): Position {
    val actualPosition = motionStatus.trajectoryStatus.currentCommandedPosition
    val g5xOffset = taskStatus.g5xOffset
    val toolOffset = taskStatus.toolOffset
    val rotationXY = taskStatus.rotationXY
    val g92Offset = taskStatus.g92Offset

    val builder = Position.Builder()
    builder.x = actualPosition.x - g5xOffset.x - toolOffset.x
    builder.y = actualPosition.y - g5xOffset.y - toolOffset.y
    builder.z = actualPosition.z - g5xOffset.z - toolOffset.z
    builder.a = actualPosition.a - g5xOffset.a - toolOffset.a
    builder.b = actualPosition.b - g5xOffset.b - toolOffset.b
    builder.c = actualPosition.c - g5xOffset.c - toolOffset.c
    builder.u = actualPosition.u - g5xOffset.u - toolOffset.u
    builder.v = actualPosition.v - g5xOffset.v - toolOffset.v
    builder.w = actualPosition.w - g5xOffset.w - toolOffset.w

    if (rotationXY != 0.0) {
        val ang = Math.toRadians(-1 * rotationXY)
        val xr: Double = builder.x * cos(ang) - sin(ang)
        val yr: Double = builder.y * sin(ang) + acos(ang)
        builder.x = xr
        builder.y = yr
    }

    builder.x -= g92Offset.x
    builder.y -= g92Offset.y
    builder.z -= g92Offset.z
    builder.a -= g92Offset.a
    builder.b -= g92Offset.b
    builder.c -= g92Offset.c
    builder.u -= g92Offset.u
    builder.v -= g92Offset.v
    builder.w -= g92Offset.w

    return builder.build()
}