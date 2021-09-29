package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.base.data.SpindleStatus
import com.mindovercnc.linuxcnc.nml.BuffDescriptor
import com.mindovercnc.linuxcnc.nml.Key
import java.nio.ByteBuffer

class SpindleStatusFactory(
    descriptor: BuffDescriptor
) : ParsingFactory<List<SpindleStatus>>(descriptor) {

    override fun parse(byteBuffer: ByteBuffer): List<SpindleStatus> {
        val numSpindles = byteBuffer.getIntForKey(Key.SpindlesCount)!!

        val spindle0Offset = byteBuffer.getIntForKey(Key.Spindle0)!!
        val spindle1Offset = byteBuffer.getIntForKey(Key.Spindle1)!!

        val result = mutableListOf<SpindleStatus>()
        for (spindleNo in 0..numSpindles) {
            val spindleOffset = spindleNo * (spindle1Offset - spindle0Offset)

            result.add(
                SpindleStatus(
                    spindleRpm = byteBuffer.getDoubleForKey(Key.Spindle0Speed, spindleOffset)!!,
                    spindleScale = byteBuffer.getDoubleForKey(Key.Spindle0Scale, spindleOffset)!! * 100,
                    cssMaximum = byteBuffer.getDoubleForKey(Key.Spindle0CssMaximum, spindleOffset)!!,
                    cssFactor = byteBuffer.getDoubleForKey(Key.Spindle0CssFactor, spindleOffset)!!, //TODO: maybe *100
                    state = byteBuffer.getIntForKey(Key.Spindle0State, spindleOffset)!!,
                    direction = SpindleStatus.Direction.values()[byteBuffer.getIntForKey(Key.Spindle0Direction, spindleOffset)!!],
                    increasing = SpindleStatus.Increasing.values()[byteBuffer.getIntForKey(Key.Spindle0Increasing, spindleOffset)!!],
                    isBrakeEngaged = byteBuffer.getBooleanForKey(Key.Spindle0Brake, spindleOffset)!!,
                    isEnabled = byteBuffer.getBooleanForKey(Key.Spindle0Enabled, spindleOffset)!!,
                    isOverrideEnabled = byteBuffer.getBooleanForKey(Key.Spindle0OverrideEnabled, spindleOffset)!!,
                    isHomed = byteBuffer.getBooleanForKey(Key.Spindle0Homed, spindleOffset)!!,
                    orientState = byteBuffer.getIntForKey(Key.Spindle0OrientState, spindleOffset)!!,
                    orientFault = byteBuffer.getIntForKey(Key.Spindle0OrientFault, spindleOffset)!!
                )
            )
        }
        return result
    }
}