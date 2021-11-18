package di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import usecase.MessagesUseCase
import usecase.TurningUseCase

val UseCaseModule = DI.Module("UseCase") {
    bindProvider {
        MessagesUseCase(instance())
    }

    bindProvider {
        TurningUseCase(instance(), instance(), instance())
    }
}