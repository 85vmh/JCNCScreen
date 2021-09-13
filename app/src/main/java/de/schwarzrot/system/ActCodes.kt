package de.schwarzrot.system

import com.mindovercnc.base.nml.BufferEntry
import java.nio.ByteBuffer

private const val MAX_CODES = 16

data class ActCodes(val list: List<Float>) {

    companion object {
        fun fromOffset(statusBuffer: ByteBuffer, entry: BufferEntry): ActCodes {
            val result = mutableListOf<Float>()
            for (i in 0..MAX_CODES) {
                val code = statusBuffer.getInt(entry.offset + 4 * i)
                if (code > 0) {
                    result.add(code / 10f)
                }
            }
            return ActCodes(result)
        }
    }
}
