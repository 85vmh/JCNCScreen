package di

import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.dummycnc.DummyCommandRepository
import com.mindovercnc.dummycnc.DummyStatusRepository
import com.mindovercnc.dummycnc.PositionMock
import com.mindovercnc.linuxcnc.CncCommandRepositoryImpl
import com.mindovercnc.linuxcnc.CncStatusRepositoryImpl
import com.mindovercnc.linuxcnc.nml.BuffDescriptor
import com.mindovercnc.linuxcnc.nml.BuffDescriptorV29
import com.mindovercnc.linuxcnc.parsing.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val RepositoryModule = DI.Module("repository") {

    bindSingleton("dummyStatus") {
        emptyFlow<CncStatus>()
    }

    bindSingleton("dummyError") {
        emptyFlow<SystemMessage>()
    }

    //bindSingleton<CncStatusRepository> { DummyStatusRepository(instance("dummyStatus"), instance("dummyError")) }
    //bindSingleton<CncCommandRepository> { DummyCommandRepository() }
    bindSingleton<CncStatusRepository> { CncStatusRepositoryImpl(instance()) }
    bindSingleton<CncCommandRepository> { CncCommandRepositoryImpl() }

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