// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("FunctionName")

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
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.*
import com.mindovercnc.base.nml.BufferDescriptor
import com.mindovercnc.dummycnc.PositionMock
import di.RepositoryModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import java.util.*

fun main() {
    application {
        MyWindow(this::exitApplication)
    }
}

@Composable
fun MyWindow(onCloseRequest: () -> Unit) = Window(onCloseRequest = onCloseRequest, title = "KtCnc") {

    //withDI(CncModule) {
    withDI(RepositoryModule) {
        val di = localDI()
        val scope = rememberCoroutineScope {
            Dispatchers.Main
        }

        val flow by di.instance<MutableStateFlow<CncStatus>>("dummy")

        remember {
            val scanner = Scanner(
                System.`in`
            )

            scope.launch {
                while (true) {
                    if (scanner.hasNext()) {
                        val x = scanner.nextLine()
                        flow.value = CncStatus(StatusState(PositionMock.mock(x.toDouble())), ErrorState(null))
                    }
                    delay(50L)
                }
            }
        }
        val statusRepository by di.instance<CncStatusRepository>()

        val sharedFlow: Flow<CncStatus> = remember { statusRepository.cncStatusFlow() }
        val xx by sharedFlow.map {
            it.statusState.positionState
        }.collectAsState(null)

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
    val absPos: Position = posState.absPos
    val g5xPos: Position = posState.g5xPos
    val toolPos: Position = posState.toolPos
    val g92Pos: Position = posState.g92Pos

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