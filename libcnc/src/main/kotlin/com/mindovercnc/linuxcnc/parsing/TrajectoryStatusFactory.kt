package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.TrajectoryStatus
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

class TrajectoryStatusFactory(
    descriptor: IBufferDescriptor
) : ParsingFactory<TrajectoryStatus>(descriptor) {


    override fun parse(byteBuffer: ByteBuffer): TrajectoryStatus {
        TODO("Not yet implemented")
    }

}