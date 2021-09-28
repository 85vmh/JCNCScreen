package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.*
import com.mindovercnc.linuxcnc.StatusReader
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

class CncStatusFactory(
    descriptor: IBufferDescriptor,
    private val taskStatusFactory: TaskStatusFactory,
    private val motionStatusFactory: MotionStatusFactory,
    private val ioStatusFactory: IoStatusFactory
) : ParsingFactory<CncStatus>(descriptor) {

    override fun parse(byteBuffer: ByteBuffer) = CncStatus(
        taskStatus = taskStatusFactory.parse(byteBuffer),
        motionStatus = motionStatusFactory.parse(byteBuffer),
        ioStatus = ioStatusFactory.parse(byteBuffer),

//        axisMask = byteBuffer.getIntForKey(IBufferDescriptor.AxisMask),
//
//        loadedToolNo = byteBuffer.getIntForKey(IBufferDescriptor.ToolInSpindle),
//        preparedPocket = byteBuffer.getIntForKey(IBufferDescriptor.PocketPrepared),
//
//        currentLine = byteBuffer.getIntForKey(IBufferDescriptor.CurrentLine),
//
//        speedInfo = parseSpeedInfo(byteBuffer),
//
//        spindleInfo = parseSpindleInfo(byteBuffer),
//
//        jointsInfo = parseJointsInfo(byteBuffer),
    )

//    private fun parseSpeedInfo(statusBuffer: ByteBuffer) = SpeedInfo(
//        rapidOverride = statusBuffer.getDoubleForKey(IBufferDescriptor.Rapidrate) * 100,
//        feedOverride = statusBuffer.getDoubleForKey(IBufferDescriptor.Feedrate) * 100,
//        currentVelocity = statusBuffer.getDoubleForKey(IBufferDescriptor.Current_vel) * 60,
//        maxVelocity = statusBuffer.getDoubleForKey(IBufferDescriptor.Max_velocity) * 60,
//        currentAcceleration = statusBuffer.getDoubleForKey(IBufferDescriptor.Acceleration),
//        maxAcceleration = statusBuffer.getDoubleForKey(IBufferDescriptor.Max_acceleration),
//    )
//
//    private fun parseSpindleInfo(statusBuffer: ByteBuffer): SpindleInfo {
//        val extra_spindle_offset = 168 //not sure why 168
//        val numSpindles = statusBuffer.getIntForKey(IBufferDescriptor.Spindles)
//        val spindles = mutableListOf<SpindleInfo.Spindle>()
//        for (spIndex in 0..numSpindles) {
//            spindles.add(
//                SpindleInfo.Spindle(
//                    spindleDirection = SpindleInfo.Spindle.SpindleDirection.values()[
//                            statusBuffer.getIntForKey(IBufferDescriptor.SpindleDir, spIndex * extra_spindle_offset)
//                    ],
//                    spindleOverride = statusBuffer.getDoubleForKey(IBufferDescriptor.SpindleScale, spIndex * extra_spindle_offset) * 100,
//                    spindleCurrentSpeed = statusBuffer.getDoubleForKey(IBufferDescriptor.SpindleSpeed, spIndex * extra_spindle_offset),
//                    spindleEnabled = statusBuffer.getBooleanForKey(IBufferDescriptor.SpindleEnabled, spIndex * extra_spindle_offset),
//                    spindleHomed = statusBuffer.getBooleanForKey(IBufferDescriptor.SpindleHomed, spIndex * extra_spindle_offset),
//                )
//            )
//        }
//        return SpindleInfo(numSpindles, spindles)
//    }
//
//    private fun parseJointsInfo(statusBuffer: ByteBuffer): JointsInfo {
//        val numJoints = statusBuffer.getIntForKey(IBufferDescriptor.Joints)
//        val joints = mutableListOf<JointsInfo.JointStatus>()
//        for (jointIndex in 0 until numJoints) {
//            joints.add(
//                JointsInfo.JointStatus(
//                    enabled = statusBuffer.getBooleanForKey("joint_${jointIndex}_enabled"),
//                    homed = statusBuffer.getBooleanForKey("joint_${jointIndex}_homed")
//                )
//            )
//        }
//        return JointsInfo(numJoints, joints)
//    }
}

