package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.*
import com.mindovercnc.linuxcnc.StatusReader
import com.mindovercnc.linuxcnc.nml.BufferEntry
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

internal enum class ActiveCodeType(val maxCodes: Int, val divideBy: Float) {
    G_CODE(16, 10f),
    M_CODE(10, 1f)
}

class TaskStatusFactory(
    descriptor: IBufferDescriptor,
    private val statusReader: StatusReader,
    private val positionFactory: PositionFactory,
) : ParsingFactory<TaskStatus>(descriptor) {

    override fun parse(byteBuffer: ByteBuffer) = TaskStatus(
        taskMode = TaskMode.values()[byteBuffer.getIntForKey(IBufferDescriptor.TaskState)],
        taskState = TaskState.values()[byteBuffer.getIntForKey(IBufferDescriptor.TaskState)],
        execState = TaskExecState.values()[(byteBuffer.getIntForKey(IBufferDescriptor.ExecState))],
        interpreterState = InterpreterState.values()[(byteBuffer.getIntForKey(IBufferDescriptor.InterpState))],
        callLevel = 0,
        motionLine = byteBuffer.getIntForKey(IBufferDescriptor.MotionLine),
        readLine = byteBuffer.getIntForKey(IBufferDescriptor.ReadLine),
        isOptionalStopEnabled = false,
        isBlockDeleteEnabled = false,
        isDigitalInputTimeout = false,
        loadedFile = readString(statusReader, IBufferDescriptor.File),
        command = null,
        g5xOffset = positionFactory.parse(byteBuffer, PositionFactory.PositionType.G5X_OFFSET),
        g5xIndex = -1,
        g92Offset = positionFactory.parse(byteBuffer, PositionFactory.PositionType.G92_OFFSET),
        rotationXY = byteBuffer.getDoubleForKey(IBufferDescriptor.RotationXY),
        toolOffset = positionFactory.parse(byteBuffer, PositionFactory.PositionType.TOOL_OFFSET),
        activeCodes = ActiveCodes(
            gCodes = parseActiveCodes(byteBuffer, descriptor[IBufferDescriptor.ActiveGCodes]!!, ActiveCodeType.G_CODE),
            mCodes = parseActiveCodes(byteBuffer, descriptor[IBufferDescriptor.ActiveMCodes]!!, ActiveCodeType.M_CODE),
        ),
        activeSettings = -0.0,
        programUnits = LengthUnit.values()[byteBuffer.getIntForKey(IBufferDescriptor.ProgramUnits)],
        intepreterErrorCode = -1,
        isTaskPaused = false,
        delayLeft = -1.0,
        mdiInputQueue = -1
    )

    private fun parseActiveCodes(statusBuffer: ByteBuffer, entry: BufferEntry, activeCodeType: ActiveCodeType): List<Float> {
        val result = mutableListOf<Float>()
        for (i in 0..activeCodeType.maxCodes) {
            val code = statusBuffer.getInt(entry.offset + 4 * i)
            if (code > 0) {
                result.add(code / activeCodeType.divideBy)
            }
        }
        return result
    }

    private fun readString(statusReader: StatusReader, key: String): String? {
        return descriptor[key]?.let {
            statusReader.getString(it.offset, it.size)
        }
    }
}