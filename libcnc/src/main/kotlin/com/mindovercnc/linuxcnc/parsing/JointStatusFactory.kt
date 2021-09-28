package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.JointStatus
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

class JointStatusFactory(
    descriptor: IBufferDescriptor
) : ParsingFactory<JointStatus>(descriptor) {

    override fun parse(byteBuffer: ByteBuffer): JointStatus {
        TODO("Not yet implemented")
    }
}