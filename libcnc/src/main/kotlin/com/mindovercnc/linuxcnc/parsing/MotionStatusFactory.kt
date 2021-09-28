package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.JointStatus
import com.mindovercnc.base.data.MotionStatus
import com.mindovercnc.base.data.SpindleStatus
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

class MotionStatusFactory(
    descriptor: IBufferDescriptor,
    private val trajectoryStatusFactory: TrajectoryStatusFactory,
    private val jointStatusFactory: JointStatusFactory,
    private val positionFactory: PositionFactory,
) : ParsingFactory<MotionStatus>(descriptor) {

    override fun parse(byteBuffer: ByteBuffer): MotionStatus {
        val trajectoryStatus = trajectoryStatusFactory.parse(byteBuffer)

        return MotionStatus(
            trajectoryStatus = trajectoryStatus,
            jointsStatus = getJointsStatus(byteBuffer, trajectoryStatus.numJoints),
            spindlesStatus = getSpindlesStatus(byteBuffer, trajectoryStatus.numSpindles),
            synchronizedDigitalInputs = emptyList(),
            synchronizedDigitalOutputs = emptyList(),
            analogInputs = emptyList(),
            analogOutputs = emptyList(),
            isOnSoftLimit = false,
            isExternalOffsetsApplied = false,
            externalOffsets = positionFactory.parse(byteBuffer, PositionFactory.PositionType.EXTERNAL_OFFSETS),
            numExtraJoints = -1
        )
    }

    private fun getJointsStatus(byteBuffer: ByteBuffer, numJoints: Int): List<JointStatus> {
//        for(jointNo in 0..numJoints){
//            //compute the offset for each joint
//            jointStatusFactory.parse(eachJointOffset)
//        }
        return emptyList()
    }

    private fun getSpindlesStatus(byteBuffer: ByteBuffer, numSpindles: Int): List<SpindleStatus> {
        return emptyList()
    }
}