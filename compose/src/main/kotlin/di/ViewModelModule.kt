package di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import screen.composables.RootScreenViewModel
import screen.composables.NotHomedViewModel

val ViewModelModule = DI.Module("ViewModel") {
    bindProvider {
        NotHomedViewModel(instance())
    }
    bindProvider {
        RootScreenViewModel(instance())
    }
}