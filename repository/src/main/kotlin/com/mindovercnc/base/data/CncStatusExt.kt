package com.mindovercnc.base.data

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

fun CncStatus.isHomed(): Boolean {
    motionStatus.jointsStatus.forEach {
        if (it.isHomed.not()) return false
    }
    return true
}

val CncStatus.isXHomed get() = motionStatus.jointsStatus[0].isHomed
val CncStatus.isZHomed get() = motionStatus.jointsStatus[1].isHomed

val CncStatus.isXHoming get() = motionStatus.jointsStatus[0].isHoming
val CncStatus.isZHoming get() = motionStatus.jointsStatus[1].isHoming

val CncStatus.isInterpreterIdle get() = taskStatus.interpreterState == InterpreterState.Idle
val CncStatus.isInMdiMode get() = taskStatus.taskMode == TaskMode.TaskModeMDI
val CncStatus.isInManualMode get() = taskStatus.taskMode == TaskMode.TaskModeManual
val CncStatus.isInAutoMode get() = taskStatus.taskMode == TaskMode.TaskModeAuto

val CncStatus.jogVelocity get() = motionStatus.trajectoryStatus.maxVelocity

val CncStatus.isEstop get() = taskStatus.taskState == TaskState.EStop

val CncStatus.isNotOn get() = taskStatus.taskState == TaskState.MachineOff || taskStatus.taskState == TaskState.EStopReset

val CncStatus.isOn get() = taskStatus.taskState == TaskState.MachineOn

val CncStatus.isMinSoftLimitOnX get() = motionStatus.jointsStatus[0].minSoftLimitExceeded
val CncStatus.isMaxSoftLimitOnX get() = motionStatus.jointsStatus[0].maxSoftLimitExceeded
val CncStatus.isMinSoftLimitOnZ get() = motionStatus.jointsStatus[1].minSoftLimitExceeded
val CncStatus.isMaxSoftLimitOnZ get() = motionStatus.jointsStatus[1].maxSoftLimitExceeded

val CncStatus.isDiameterMode get() = taskStatus.activeCodes.gCodes.contains(7.0f)

val CncStatus.g53Position get() = motionStatus.trajectoryStatus.currentActualPosition

val CncStatus.isSpindleOn
    get() = motionStatus.spindlesStatus[0].direction == SpindleStatus.Direction.REVERSE ||
            motionStatus.spindlesStatus[0].direction == SpindleStatus.Direction.FORWARD

val CncStatus.currentToolNo get() = ioStatus.toolStatus.currentLoadedTool

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
