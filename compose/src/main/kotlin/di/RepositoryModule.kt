package di

import com.mindovercnc.base.*
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.linuxcnc.*
import com.mindovercnc.linuxcnc.nml.BuffDescriptor
import com.mindovercnc.linuxcnc.nml.BuffDescriptorV29
import com.mindovercnc.linuxcnc.parsing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.util.prefs.Preferences

val RepositoryModule = DI.Module("repository") {
    bindSingleton<CncStatusRepository> { CncStatusRepositoryImpl(instance("app_scope"), instance()) }
    bindSingleton<MessagesRepository> { MessagesRepositoryImpl(instance("app_scope")) }
    bindSingleton<CncCommandRepository> { CncCommandRepositoryImpl() }
    bindSingleton<HalRepository> { HalRepositoryImpl(instance("app_scope")) }
    bindSingleton<IniFileRepository> { IniFileRepositoryImpl(instance("ini")) }
    bindSingleton<VarFileRepository> { VarFileRepositoryImpl(instance("app_scope"), instance("var")) }
    bindSingleton<ToolFileRepository> { ToolFileRepositoryImpl(instance("app_scope"), instance("tool")) }
    bindSingleton { Preferences.userRoot() }
    bindSingleton<SettingsRepository> { SettingsRepositoryImpl(instance()) }
}

fun iniFileModule(filePath: String) = DI.Module("ini") {
    bindProvider("ini") { filePath }
}

fun toolFileModule(filePath: String) = DI.Module("tool") {
    bindProvider("tool") { filePath }
}

fun varFileModule(filePath: String) = DI.Module("var") {
    bindProvider("var") { filePath }
}

fun appScopeModule(scope: CoroutineScope) = DI.Module("app_scope") {
    bindProvider("app_scope") { scope }
}

val BuffDescriptorModule = DI.Module("buffDescriptor") {
    bindSingleton<BuffDescriptor> {
        BuffDescriptorV29()
    }
}

val ParseFactoryModule = DI.Module("parseFactory") {
    bindSingleton { PositionFactory(instance()) }
    bindSingleton { TaskStatusFactory(instance(), instance()) }
    bindSingleton { TrajectoryStatusFactory(instance(), instance()) }
    bindSingleton { JointStatusFactory(instance()) }
    bindSingleton { SpindleStatusFactory(instance()) }
    bindSingleton { MotionStatusFactory(instance(), instance(), instance(), instance(), instance()) }
    bindSingleton { IoStatusFactory(instance()) }
    bindSingleton { CncStatusFactory(instance(), instance(), instance(), instance()) }
}