package com.mindovercnc.base.data

import com.mindovercnc.base.nml.IBufferDescriptor
import java.nio.ByteBuffer

data class PositionState(
    val absPos: Pos,
    val g5xPos: Pos,
    val toolPos: Pos,
    val g92Pos: Pos,
) {
    constructor(statusBuffer: ByteBuffer, bufDesc: IBufferDescriptor) : this(
        absPos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.AbsPosX]!!),
        g5xPos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.G5xOffsX]!!),
        toolPos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.ToolOffsX]!!),
        g92Pos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.G92OffsX]!!)
    )
}