package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.IoStatus
import com.mindovercnc.base.data.ToolStatus
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

class IoStatusFactory(descriptor: IBufferDescriptor) : ParsingFactory<IoStatus>(descriptor) {

    override fun parse(byteBuffer: ByteBuffer) = IoStatus(
        cycleTime = 0.0,
        reason = -1,
        isM6Fault = false,
        toolStatus = ToolStatus(
            pocketPrepared = byteBuffer.getIntForKey(IBufferDescriptor.PocketPrepared),
            currentLoadedTool = byteBuffer.getIntForKey(IBufferDescriptor.ToolInSpindle)
        ),
        isMistOn = byteBuffer.getBooleanForKey(IBufferDescriptor.CoolMist),
        isFloodOn = byteBuffer.getBooleanForKey(IBufferDescriptor.CoolFlood),
        isEstopActive = byteBuffer.getBooleanForKey(IBufferDescriptor.Estop),
        isLubePumpOn = byteBuffer.getBooleanForKey(IBufferDescriptor.Lube),
        isLubeLevelOk = false
    )
}