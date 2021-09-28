package com.mindovercnc.base.data

data class CncStatus(
    val taskStatus: TaskStatus,
    val motionStatus: MotionStatus,
    val ioStatus: IoStatus
)