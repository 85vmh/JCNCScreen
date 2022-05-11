// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("FunctionName")

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mindovercnc.database.DbInitializer
import com.mindovercnc.linuxcnc.CncInitializer
import di.*
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import themes.AppTheme
import java.io.File


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

    CncInitializer()
    DbInitializer()

    application {
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
    undecorated = true,
    state = windowState
) {

    val scope = rememberCoroutineScope {
        Dispatchers.IO
    }
    withDI(
        iniFileModule(filePath),
        appScopeModule(scope),
        AppModule,
        ScreenModelModule,
        UseCaseModule,
        RepositoryModule,
        ParseFactoryModule,
        BuffDescriptorModule
    ) {
        val statusWatcher by rememberInstance<StatusWatcher>()
        statusWatcher.launchIn(scope)
        AppTheme {
            CncApp()
        }
    }
}