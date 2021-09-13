package com.mindovercnc.base.data

import com.mindovercnc.base.nml.BufferEntry
import java.nio.ByteBuffer

data class Pos(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val a: Double = 0.0,
    val b: Double = 0.0,
    val c: Double = 0.0,
    val u: Double = 0.0,
    val v: Double = 0.0,
    val w: Double = 0.0,
) {
    companion object {
        fun fromOffset(statusBuffer: ByteBuffer, entry: BufferEntry): Pos {
            return Pos(
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
    }
}
