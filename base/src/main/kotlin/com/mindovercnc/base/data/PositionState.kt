package com.mindovercnc.base.data

import com.mindovercnc.base.nml.IBufferDescriptor
import java.nio.ByteBuffer

data class PositionState(
    val absPos: Position,
    val g5xPos: Position,
    val toolPos: Position,
    val g92Pos: Position,
) {
    constructor(statusBuffer: ByteBuffer, bufDesc: IBufferDescriptor) : this(
        absPos = Position.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.AbsPosX]!!),
        g5xPos = Position.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.G5xOffsX]!!),
        toolPos = Position.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.ToolOffsX]!!),
        g92Pos = Position.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.G92OffsX]!!)
    )
}