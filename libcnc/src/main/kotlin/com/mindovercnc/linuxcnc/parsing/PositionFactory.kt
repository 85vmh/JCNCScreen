package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.Position
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

class PositionFactory(private val descriptor: IBufferDescriptor) {

    fun parse(statusBuffer: ByteBuffer, positionType: PositionType): Position {
        val entry = descriptor[positionType.descriptorKey]!!
        return Position(
            x = statusBuffer.getDouble(entry.offset),
            y = statusBuffer.getDouble(entry.offset + 8),
            z = statusBuffer.getDouble(entry.offset + 16),
            a = statusBuffer.getDouble(entry.offset + 24),
            b = statusBuffer.getDouble(entry.offset + 32),
            c = statusBuffer.getDouble(entry.offset + 40),
            u = statusBuffer.getDouble(entry.offset + 48),
            v = statusBuffer.getDouble(entry.offset + 56),
            w = statusBuffer.getDouble(entry.offset + 64),
        )
    }

    enum class PositionType(val descriptorKey: String) {
        G5X_OFFSET(IBufferDescriptor.G5xOffsX),
        G92_OFFSET(IBufferDescriptor.G92OffsX),
        TOOL_OFFSET(IBufferDescriptor.ToolOffsX),
        ABS_POSITION(IBufferDescriptor.AbsPosX),
        DTG(IBufferDescriptor.DtgX),
        EXTERNAL_OFFSETS(""),
    }
}