package di

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.CncStatusRepositoryImpl
import com.mindovercnc.linuxcnc.CncInitializer
import com.mindovercnc.linuxcnc.ErrorReader
import com.mindovercnc.linuxcnc.StatusReader
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val RepositoryModule = DI.Module("repository") {
    bindSingleton<CncStatusRepository> { CncStatusRepositoryImpl(CncInitializer, StatusReader(), ErrorReader()) }

}