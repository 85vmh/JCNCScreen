package de.schwarzrot.app

import com.mindovercnc.linuxcnc.StatusReader
import de.schwarzrot.bean.Position
import com.mindovercnc.linuxcnc.nml.BufferDescriptor
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import java.nio.ByteBuffer
import java.util.*

object IndependentTest {
    private val bufDesc: IBufferDescriptor = BufferDescriptor()

    @JvmStatic
    fun main(args: Array<String>) {
        val statusReader = StatusReader(updateListener = object : StatusReader.StatusUpdateListener {
            override fun onInitialStatus(statusBuffer: ByteBuffer?) {
                TODO("Not yet implemented")
            }

            override fun onStatusUpdated(statusBuffer: ByteBuffer) {
                logPosition(statusBuffer)
            }
        })

        val tt = object : TimerTask() {
            override fun run() {
                statusReader.update()
                println("tada ")
            }
        }
        Timer().scheduleAtFixedRate(tt, 0, 100L)
    }

    private fun logPosition(statusBuffer: ByteBuffer) {
        val e = bufDesc[IBufferDescriptor.AbsPosX]!!
        val p = Position()

        p.x = statusBuffer.getDouble(e.offset)
        p.y = statusBuffer.getDouble(e.offset + 8)
        p.z = statusBuffer.getDouble(e.offset + 16)
        p.a = statusBuffer.getDouble(e.offset + 24)
        p.b = statusBuffer.getDouble(e.offset + 32)
        p.c = statusBuffer.getDouble(e.offset + 40)
        p.u = statusBuffer.getDouble(e.offset + 48)
        p.v = statusBuffer.getDouble(e.offset + 56)
        p.w = statusBuffer.getDouble(e.offset + 64)
        println("---Position: $p")
    }
}
