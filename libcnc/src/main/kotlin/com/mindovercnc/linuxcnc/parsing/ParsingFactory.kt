package com.mindovercnc.linuxcnc.parsing

import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer

abstract class ParsingFactory<T>(val descriptor: IBufferDescriptor) {
    abstract fun parse(byteBuffer: ByteBuffer): T

    internal fun ByteBuffer.getIntForKey(key: String, additionalOffset: Int = 0): Int {
        return this.getInt(descriptor[key]!!.offset + additionalOffset)
    }

    internal fun ByteBuffer.getDoubleForKey(key: String, additionalOffset: Int = 0): Double {
        return this.getDouble(descriptor[key]!!.offset + additionalOffset)
    }

    internal fun ByteBuffer.getBooleanForKey(key: String, additionalOffset: Int = 0): Boolean {
        return this.getInt(descriptor[key]!!.offset + additionalOffset) != 0
    }
}