package di

import com.mindovercnc.base.*
import com.mindovercnc.linuxcnc.*
import com.mindovercnc.linuxcnc.nml.BuffDescriptor
import com.mindovercnc.linuxcnc.nml.BuffDescriptorV29
import com.mindovercnc.linuxcnc.parsing.*
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.io.File
import java.util.prefs.Preferences

val RepositoryModule = DI.Module("repository") {
    bindSingleton<CncStatusRepository> { CncStatusRepositoryImpl(instance("app_scope"), instance()) }
    bindSingleton<MessagesRepository> { MessagesRepositoryImpl(instance("app_scope")) }
    bindSingleton<CncCommandRepository> { CncCommandRepositoryImpl() }
    bindSingleton<HalRepository> { HalRepositoryImpl(instance("app_scope")) }
    bindSingleton<IniFileRepository> { IniFileRepositoryImpl(instance()) }
    bindSingleton<VarFileRepository> {
        VarFileRepositoryImpl(instance("app_scope"), instance())
    }
    bindSingleton<ToolsRepository> {
        ToolsRepositoryImpl(instance("app_scope"), instance())
    }
    bindSingleton<FileSystemRepository> {
        val iniRepo: IniFileRepository = instance()
        val filePath = iniRepo.getIniFile().programPrefix
        FileSystemRepositoryImpl(instance("app_scope"), filePath)
    }
    bindSingleton { Preferences.userRoot() }
    bindSingleton<SettingsRepository> { SettingsRepositoryImpl(instance()) }
    bindSingleton<ActiveLimitsRepository> { ActiveLimitsRepository() }
    bindSingleton<CuttingInsertsRepository> { CuttingInsertsRepositoryImpl() }

    bindProvider {
        ToolFilePath(instance<IniFileRepository>().getIniFile().toolTableFile)
    }

    bindProvider {
        VarFilePath(instance<IniFileRepository>().getIniFile().parameterFile)
    }

    bindSingleton<GCodeRepository> {
        GCodeRepositoryImpl(
            iniFilePath = instance(),
            toolFilePath = instance(),
            varFilePath = instance()
        )
    }
}

fun iniFileModule(iniFile: File) = DI.Module("ini") {
    bindProvider { IniFilePath(iniFile) }
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