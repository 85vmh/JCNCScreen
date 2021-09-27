package di

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.dummycnc.DummyStatusRepository
import com.mindovercnc.dummycnc.PositionMock
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val RepositoryModule = DI.Module("repository") {

    bindSingleton("dummy") {
        MutableStateFlow(CncStatus(StatusState(PositionMock.mock()), ErrorState(null)))
    }

    bindSingleton<CncStatusRepository> { DummyStatusRepository(instance<MutableStateFlow<CncStatus>>("dummy")) }

}