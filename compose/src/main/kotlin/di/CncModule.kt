package di

import com.mindovercnc.base.IStatusReader
import com.mindovercnc.base.Initializer
import com.mindovercnc.linuxcnc.CncInitializer
import com.mindovercnc.linuxcnc.StatusReader
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val CncModule = DI.Module("cnc") {
    bindSingleton<Initializer> { CncInitializer }
    bindSingleton<IStatusReader> { StatusReader() }
}