// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("FunctionName")

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.*
import di.BuffDescriptorModule
import di.ParseFactoryModule
import di.RepositoryModule
import di.VisualTurning
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import screen.RootScreenView
import screen.composables.NumPadKey
import screen.composables.NumPadView

fun main() {
    application {
        val windowState = rememberWindowState(size = WindowSize(1024.dp, 768.dp))
        MyWindow(windowState, this::exitApplication)
    }
}

@Composable
fun MyWindow(
    windowState: WindowState,
    onCloseRequest: () -> Unit
) = Window(
    onCloseRequest = onCloseRequest,
    title = "KtCnc",
    state = windowState
) {

    //withDI(CncModule) {
    withDI(RepositoryModule, ParseFactoryModule, BuffDescriptorModule) {
        val di = localDI()

        val statusRepository by di.instance<CncStatusRepository>()
        val commandRepository by di.instance<CncCommandRepository>()

        val cncStatusSharedFlow: Flow<CncStatus> = remember { statusRepository.cncStatusFlow() }
        val errorsSharedFlow: Flow<SystemMessage> = remember { statusRepository.errorFlow() }

        val cncStatus by cncStatusSharedFlow.map {
            PositionState(
                it.motionStatus.trajectoryStatus.currentCommandedPosition,
                it.motionStatus.trajectoryStatus.currentActualPosition,
                it.taskStatus.g5xOffset,
                it.taskStatus.toolOffset
            )
        }.collectAsState(null)

        val errors by errorsSharedFlow.collectAsState(null)

        //val statusBuffer by statusReader.status.collectAsState(null)
        MaterialTheme {
            //RootScreenView()

            Content(cncStatus, errors, commandRepository)
            //VisualTurning()
        }
    }
}

@Composable
fun Content(xx: PositionState?, errors: SystemMessage?, commandRepository: CncCommandRepository) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.LightGray)) {
            Row(modifier = Modifier.fillMaxSize()) {
                if (xx != null) {
                    displaySomething(xx, Modifier.weight(1f).fillMaxHeight())
                }
                Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
                NumPadView(modifier = Modifier.fillMaxHeight())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errors != null) {
            println("----errors: $errors")
            displayError(errors)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Button(onClick = {
                commandRepository.setTaskState(TaskState.EStop)
            }) {
                Text("E-Stop Activate")
            }
            Button(onClick = {
                commandRepository.setTaskState(TaskState.EStopReset)
            }) {
                Text("E-Stop Reset")
            }
            Button(onClick = {
                commandRepository.setTaskState(TaskState.MachineOn)
            }) {
                Text("Machine ON")
            }
            Button(onClick = {
                commandRepository.setTaskState(TaskState.MachineOff)
            }) {
                Text("Machine OFF")
            }
            Button(onClick = {
                commandRepository.homeAll()
            }) {
                Text("Home All")
            }
            Button(onClick = {
                commandRepository.unHomeAll()
            }) {
                Text("UnHome All")
            }
        }
    }
}

@Composable
@Preview
fun displaySomething(posState: PositionState, modifier: Modifier) {
    val absPos: Position = posState.absPos
    val g5xPos: Position = posState.g5xPos
    val toolPos: Position = posState.toolPos
    val g92Pos: Position = posState.g92Pos

    Column(modifier = modifier) {
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

}

@Composable
@Preview
fun displayError(systemMessage: SystemMessage) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Time: ${
            systemMessage.time.toInstant().toEpochMilli()
        } Type: ${systemMessage.type} Message: ${systemMessage.message}"
    )
}