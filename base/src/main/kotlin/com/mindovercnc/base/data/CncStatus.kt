package com.mindovercnc.base.data

data class CncStatus(
    val absPosition: Position,
    val g5xOffset: Position,
    val toolOffset: Position,
    val g92Offset: Position,
    val distance2Go: Position,
    val xyRotation: Double,

    val activeCodes: ActiveCodes,

    val taskMode: TaskMode? = null,
    val taskState: TaskState? = null,
    val taskExecState: TaskExecState? = null,
    val interpreterState: InterpreterState? = null,

    val axisMask: Int,
    val lengthUnit: LengthUnit? = null,

    val loadedToolNo: Int,
    val preparedPocket: Int,

    val readLine: Int,
    val motionLine: Int,
    val currentLine: Int,
    val loadedFile: String? = null,

    val speedInfo: SpeedInfo,
    val spindleInfo: SpindleInfo,
    val jointsInfo: JointsInfo,
)