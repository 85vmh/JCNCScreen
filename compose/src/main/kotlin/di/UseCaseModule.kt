package di

import codegen.ManualTurningHelper
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import usecase.ManualTurningUseCase
import usecase.MessagesUseCase

val UseCaseModule = DI.Module("UseCase") {
    bindSingleton {
        MessagesUseCase(instance())
    }

    bindSingleton {
        ManualTurningUseCase(instance("app_scope"), instance(), instance(), instance(), instance(), instance())
    }
    bindSingleton {
        ManualTurningHelper(instance())
    }
}