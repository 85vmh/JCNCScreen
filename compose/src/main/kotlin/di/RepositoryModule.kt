package di

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.dummycnc.DummyStatusRepository
import com.mindovercnc.dummycnc.PositionMock
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

    bindSingleton("dummy") {
        emptyFlow<CncStatus>()
    }

    bindSingleton<CncStatusRepository> { DummyStatusRepository(instance("dummy")) }
    //bindSingleton<CncStatusRepository> { CncStatusRepositoryImpl(instance()) }

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