// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("FunctionName")

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mindovercnc.base.HalRepository
import com.mindovercnc.linuxcnc.CncInitializer
import di.*
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import screen.BaseScreenView
import screen.composables.VtkState
import screen.composables.VtkView
import vtk.*
import java.io.File
import java.lang.IllegalArgumentException


fun main(args: Array<String>) {
    //val process = Runtime.getRuntime().exec("linuxcnc '/home/vasimihalca/Work/linuxcnc-dev/configs/sim/axis/lathe.ini'")
    //Thread.sleep(1000L)

//    if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
//        for (lib in vtkNativeLibrary.values()) {
//            if (!lib.IsLoaded()) {
//                println(lib.GetLibraryName() + " not loaded")
//            }
//        }
//    }
//    vtkNativeLibrary.DisableOutputWindow(null)

    val iniFilePath = args.firstOrNull()?.takeIf { File(it).exists() } ?: throw IllegalArgumentException(".ini file not found")
    application {
        CncInitializer.initialize()
        val windowState = rememberWindowState(width = 1024.dp, height = 768.dp)
        MyWindow(windowState, iniFilePath) {
            //process.destroy()
            //process.waitFor()
            this.exitApplication()
        }
    }
}

@Composable
fun MyWindow(
    windowState: WindowState,
    filePath: String,
    onCloseRequest: () -> Unit
) = Window(
    onCloseRequest = onCloseRequest,
    title = "KtCnc",
    focusable = false,
    //undecorated = true,
    state = windowState
) {

    val scope = rememberCoroutineScope {
        Dispatchers.IO
    }
    withDI(
        iniFileModule(filePath),
        appScopeModule(scope),
        ViewModelModule,
        UseCaseModule,
        RepositoryModule,
        ParseFactoryModule,
        BuffDescriptorModule
    ) {

        MaterialTheme {
            BaseScreenView()
//            val state = remember {
//
//
//                val cone = vtkConeSource()
//                cone.SetResolution(8)
//
//                val coneMapper = vtkPolyDataMapper()
//                coneMapper.SetInputConnection(cone.GetOutputPort())
//
//                val coneActor = vtkActor()
//                coneActor.SetMapper(coneMapper)
//                VtkState(coneActor)
//            }
//            VtkView(state, modifier = Modifier.fillMaxSize())
        }
    }
}