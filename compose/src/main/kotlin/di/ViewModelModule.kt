package di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import screen.viewmodel.OdTurningViewModel

val ViewModelModule = DI.Module("ViewModel") {
    bindProvider {
        OdTurningViewModel(instance())
    }
}