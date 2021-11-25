package di

import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.IniFileRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.linuxcnc.CncCommandRepositoryImpl
import com.mindovercnc.linuxcnc.CncStatusRepositoryImpl
import com.mindovercnc.linuxcnc.HalRepositoryImpl
import com.mindovercnc.linuxcnc.IniFileRepositoryImpl
import com.mindovercnc.linuxcnc.nml.BuffDescriptor
import com.mindovercnc.linuxcnc.nml.BuffDescriptorV29
import com.mindovercnc.linuxcnc.parsing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val RepositoryModule = DI.Module("repository") {

    bindSingleton("dummyStatus") {
        emptyFlow<CncStatus>()
    }

    bindSingleton("dummyError") {
        emptyFlow<SystemMessage>()
    }

    bindSingleton<CncStatusRepository> { CncStatusRepositoryImpl(instance("app_scope"), instance()) }
    bindSingleton<CncCommandRepository> { CncCommandRepositoryImpl() }
    bindSingleton<HalRepository> { HalRepositoryImpl(instance("app_scope")) }
    bindSingleton<IniFileRepository> { IniFileRepositoryImpl("/home/vasimihalca/Work/linuxcnc-dev/configs/sim/axis/lathe.ini") }
}

fun appScopeModule(scope: CoroutineScope) = DI.Module("app_scope") {
    bindProvider<CoroutineScope>("app_scope") { scope }
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