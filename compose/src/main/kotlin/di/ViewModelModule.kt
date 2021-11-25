package di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import screen.viewmodel.CoordinatesViewModel
import screen.viewmodel.NotHomedViewModel

val ViewModelModule = DI.Module("ViewModel") {
    bindProvider {
        NotHomedViewModel(instance())
    }
    bindProvider {
        CoordinatesViewModel(instance())
    }
}