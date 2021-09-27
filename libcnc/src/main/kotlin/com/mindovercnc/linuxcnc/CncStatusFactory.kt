package com.mindovercnc.linuxcnc

import com.mindovercnc.base.data.*
import com.mindovercnc.linuxcnc.nml.BufferDescriptor
import com.mindovercnc.linuxcnc.nml.BufferEntry
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

internal enum class ActiveCodeType(val maxCodes: Int, val divideBy: Float) {
    G_CODE(16, 10f),
    M_CODE(10, 1f)
}

internal object CncStatusFactory {

    private val descriptor: IBufferDescriptor = BufferDescriptor()

    fun parse(byteBuffer: ByteBuffer, statusReader: StatusReader) = CncStatus(
        absPosition = parsePosition(byteBuffer, descriptor[IBufferDescriptor.AbsPosX]!!),
        g5xOffset = parsePosition(byteBuffer, descriptor[IBufferDescriptor.G5xOffsX]!!),
        toolOffset = parsePosition(byteBuffer, descriptor[IBufferDescriptor.ToolOffsX]!!),
        g92Offset = parsePosition(byteBuffer, descriptor[IBufferDescriptor.G92OffsX]!!),
        distance2Go = parsePosition(byteBuffer, descriptor[IBufferDescriptor.Distance2Go]!!),
        xyRotation = byteBuffer.getDoubleForKey(IBufferDescriptor.RotationXY),

        activeCodes = ActiveCodes(
            gCodes = parseActiveCodes(byteBuffer, descriptor[IBufferDescriptor.ActiveGCodes]!!, ActiveCodeType.G_CODE),
            mCodes = parseActiveCodes(byteBuffer, descriptor[IBufferDescriptor.ActiveMCodes]!!, ActiveCodeType.M_CODE),
        ),

        taskMode = TaskMode.fromInt(byteBuffer.getIntForKey(IBufferDescriptor.TaskState)),
        taskState = TaskState.fromInt(byteBuffer.getIntForKey(IBufferDescriptor.TaskState)),
        taskExecState = TaskExecState.values()[(byteBuffer.getIntForKey(IBufferDescriptor.ExecState))],
        interpreterState = InterpreterState.values()[(byteBuffer.getIntForKey(IBufferDescriptor.InterpState))],

        axisMask = byteBuffer.getIntForKey(IBufferDescriptor.AxisMask),
        lengthUnit = LengthUnit.values()[byteBuffer.getIntForKey(IBufferDescriptor.ProgramUnits)],

        loadedToolNo = byteBuffer.getIntForKey(IBufferDescriptor.ToolInSpindle),
        preparedPocket = byteBuffer.getIntForKey(IBufferDescriptor.PocketPrepared),

        currentLine = byteBuffer.getIntForKey(IBufferDescriptor.CurrentLine),
        readLine = byteBuffer.getIntForKey(IBufferDescriptor.ReadLine),
        motionLine = byteBuffer.getIntForKey(IBufferDescriptor.MotionLine),
        loadedFile = getLoadedFileName(statusReader),

        speedInfo = parseSpeedInfo(byteBuffer),

        spindleInfo = parseSpindleInfo(byteBuffer),

        jointsInfo = parseJointsInfo(byteBuffer),
    )

    private fun getLoadedFileName(statusReader: StatusReader): String? {
        return descriptor[IBufferDescriptor.File]?.let {
            statusReader.getString(it.offset, it.size)
        }
    }

    private fun parsePosition(statusBuffer: ByteBuffer, entry: BufferEntry): Position {
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

    private fun parseSpeedInfo(statusBuffer: ByteBuffer) = SpeedInfo(
        rapidOverride = statusBuffer.getDoubleForKey(IBufferDescriptor.Rapidrate) * 100,
        feedOverride = statusBuffer.getDoubleForKey(IBufferDescriptor.Feedrate) * 100,
        currentVelocity = statusBuffer.getDoubleForKey(IBufferDescriptor.Current_vel) * 60,
        maxVelocity = statusBuffer.getDoubleForKey(IBufferDescriptor.Max_velocity) * 60,
        currentAcceleration = statusBuffer.getDoubleForKey(IBufferDescriptor.Acceleration),
        maxAcceleration = statusBuffer.getDoubleForKey(IBufferDescriptor.Max_acceleration),
    )

    private fun parseSpindleInfo(statusBuffer: ByteBuffer): SpindleInfo {
        val extra_spindle_offset = 168 //not sure why 168
        val numSpindles = statusBuffer.getIntForKey(IBufferDescriptor.Spindles)
        val spindles = mutableListOf<SpindleInfo.Spindle>()
        for (spIndex in 0..numSpindles) {
            spindles.add(
                SpindleInfo.Spindle(
                    spindleDirection = SpindleInfo.Spindle.SpindleDirection.values()[
                            statusBuffer.getIntForKey(IBufferDescriptor.SpindleDir, spIndex * extra_spindle_offset)
                    ],
                    spindleOverride = statusBuffer.getDoubleForKey(IBufferDescriptor.SpindleScale, spIndex * extra_spindle_offset) * 100,
                    spindleCurrentSpeed = statusBuffer.getDoubleForKey(IBufferDescriptor.SpindleSpeed, spIndex * extra_spindle_offset),
                    spindleEnabled = statusBuffer.getBooleanForKey(IBufferDescriptor.SpindleEnabled, spIndex * extra_spindle_offset),
                    spindleHomed = statusBuffer.getBooleanForKey(IBufferDescriptor.SpindleHomed, spIndex * extra_spindle_offset),
                )
            )
        }
        return SpindleInfo(numSpindles, spindles)
    }

    private fun parseJointsInfo(statusBuffer: ByteBuffer): JointsInfo {
        val numJoints = statusBuffer.getIntForKey(IBufferDescriptor.Joints)
        val joints = mutableListOf<JointsInfo.JointStatus>()
        for (jointIndex in 0 until numJoints) {
            joints.add(
                JointsInfo.JointStatus(
                    enabled = statusBuffer.getBooleanForKey("joint_${jointIndex}_enabled"),
                    homed = statusBuffer.getBooleanForKey("joint_${jointIndex}_homed")
                )
            )
        }
        return JointsInfo(numJoints, joints)
    }

    private fun ByteBuffer.getIntForKey(key: String, additionalOffset: Int = 0): Int {
        return this.getInt(descriptor[key]!!.offset + additionalOffset)
    }

    private fun ByteBuffer.getDoubleForKey(key: String, additionalOffset: Int = 0): Double {
        return this.getDouble(descriptor[key]!!.offset + additionalOffset)
    }

    private fun ByteBuffer.getBooleanForKey(key: String, additionalOffset: Int = 0): Boolean {
        return this.getInt(descriptor[key]!!.offset + additionalOffset) != 0
    }
}

