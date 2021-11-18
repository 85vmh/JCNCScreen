// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("FunctionName")

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.Position
import com.mindovercnc.base.data.PositionState
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.linuxcnc.CncInitializer
import di.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import screen.BaseScreenView
import usecase.model.SpindleControlMode

fun main() {
    //val process = Runtime.getRuntime().exec("linuxcnc '/home/vasimihalca/Work/linuxcnc-dev/configs/sim/axis/lathe.ini'")
    //Thread.sleep(1000L)
    application {
        CncInitializer.initialize()
        val windowState = rememberWindowState(size = WindowSize(1024.dp, 768.dp))
        MyWindow(windowState) {
            //process.destroy()
            //process.waitFor()
            this.exitApplication()
        }
    }
}

@Composable
fun MyWindow(
    windowState: WindowState,
    onCloseRequest: () -> Unit
) = Window(
    onCloseRequest = onCloseRequest,
    title = "KtCnc",
    //undecorated = true,
    state = windowState
) {

    //withDI(CncModule) {
    withDI(ViewModelModule, UseCaseModule, RepositoryModule, ParseFactoryModule, BuffDescriptorModule) {
        val di = localDI()

        val statusRepository by di.instance<CncStatusRepository>()
        val commandRepository by di.instance<CncCommandRepository>()
        val halRepository by di.instance<HalRepository>()

//        halRepository.getJoystickStatus()
//            .distinctUntilChanged()
//            .onEach {
//                println("-----JoystickStatus value is: $it")
//            }
//            .flowOn(Dispatchers.IO)
//            .launchIn(GlobalScope)

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
            BaseScreenView()
        }
    }
}

@Composable
@Preview
fun displaySomething(posState: PositionState, modifier: Modifier) {
    val absPos: Position = posState.absPos

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "X: ${absPos.x} Y: ${absPos.y} Z: ${absPos.z}"
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}