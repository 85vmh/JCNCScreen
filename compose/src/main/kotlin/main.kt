// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("FunctionName")

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mindovercnc.base.IStatusReader
import com.mindovercnc.base.Initializer
import com.mindovercnc.base.PosStateMapper
import com.mindovercnc.base.data.Pos
import com.mindovercnc.base.data.PositionState
import com.mindovercnc.base.mapUsing
import com.mindovercnc.base.nml.BufferDescriptor
import di.DummyModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import java.nio.ByteBuffer

fun main() {
    application {
        MyWindow(this::exitApplication)
    }
}

@Composable
fun MyWindow(onCloseRequest: () -> Unit) = Window(onCloseRequest = onCloseRequest, title = "KtCnc") {

    //withDI(CncModule) {
    withDI(DummyModule) {
        val di = localDI()
        val initializer by di.instance<Initializer>()
        initializer.initialize()

        val statusReader by di.instance<IStatusReader>()

        val descriptor = BufferDescriptor()
        val sharedFlow: SharedFlow<ByteBuffer?> =
            statusReader.launch().shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly)
        val mapper = PosStateMapper(descriptor)
        val xx by sharedFlow.mapUsing(mapper).collectAsState(null)

        //val statusBuffer by statusReader.status.collectAsState(null)
        MaterialTheme {
            Content(xx)
        }
    }
}

@Composable
fun Content(xx: PositionState?) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (xx != null) {
            displaySomething(xx)
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
fun displaySomething(posState: PositionState) {
    val absPos: Pos = posState.absPos
    val g5xPos: Pos = posState.g5xPos
    val toolPos: Pos = posState.toolPos
    val g92Pos: Pos = posState.g92Pos

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