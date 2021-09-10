// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mindovercnc.linuxcnc.Initializer
import com.mindovercnc.linuxcnc.StatusReader
import com.mindovercnc.linuxcnc.nml.BufferDescriptor
import com.mindovercnc.linuxcnc.nml.IBufferDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

fun main() {
    Initializer.loadLibraries()


    application {
        Window(onCloseRequest = ::exitApplication, title = "KtCnc") {
            var statusBuffer : ByteBuffer? = null
            val statusReader = StatusReader(object: StatusReader.StatusUpdateListener{
                override fun onInitialStatus(initialStatusBuffer: ByteBuffer?) {

                }

                override fun onStatusUpdated(updatedStatusBuffer: ByteBuffer) {
                    statusBuffer = updatedStatusBuffer
                    println("new status buffer: ${System.currentTimeMillis()}")
                    val bufDesc = BufferDescriptor()

                    val absPos: Pos = Pos.fromOffset(statusBuffer!!, bufDesc[IBufferDescriptor.AbsPosX]!!)
                    val g5xPos: Pos = Pos.fromOffset(statusBuffer!!, bufDesc[IBufferDescriptor.G5xOffsX]!!)
                    val toolPos: Pos = Pos.fromOffset(statusBuffer!!, bufDesc[IBufferDescriptor.ToolOffsX]!!)
                    val g92Pos: Pos = Pos.fromOffset(statusBuffer!!, bufDesc[IBufferDescriptor.G92OffsX]!!)

                    println("---absPos$absPos")
                    println("---g5xPos$g5xPos")
                    println("---toolPos$toolPos")
                    println("---g92Pos$g92Pos")
                }

            })
            CoroutineScope(Dispatchers.IO).launch {
                statusReader.launch()
            }

            //val statusBuffer by statusReader.status.collectAsState(null)
            MaterialTheme {
                Content(statusBuffer)
            }
        }
    }
}

@Composable
fun Content(statusBuffer: ByteBuffer?) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (statusBuffer != null) {
            displaySomething(statusBuffer)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Button(onClick = {

            }) {
                Text("Spindle")
            }
            Button(onClick = {}) {
                Text("Feed Rate")
            }
            Button(onClick = {}) {
                Text("Tools")
            }
            Button(onClick = {}) {
                Text("Position")
            }
        }
    }
}

@Composable
@Preview
fun displaySomething(statusBuffer: ByteBuffer) {
    val bufDesc = BufferDescriptor()

    val absPos: Pos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.AbsPosX]!!)
    val g5xPos: Pos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.G5xOffsX]!!)
    val toolPos: Pos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.ToolOffsX]!!)
    val g92Pos: Pos = Pos.fromOffset(statusBuffer, bufDesc[IBufferDescriptor.G92OffsX]!!)

    println("---absPos$absPos")
    println("---g5xPos$g5xPos")
    println("---toolPos$toolPos")
    println("---g92Pos$g92Pos")

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "X: ${absPos.x} Y: ${absPos.y} Z: ${absPos.z}"
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "X: ${g5xPos.x} Y: ${g5xPos.y} Z: ${g5xPos.z}"
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "X: ${toolPos.x} Y: ${toolPos.y} Z: ${toolPos.z}"
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "X: ${g92Pos.x} Y: ${g92Pos.y} Z: ${g92Pos.z}"
    )
    Spacer(modifier = Modifier.height(16.dp))
}