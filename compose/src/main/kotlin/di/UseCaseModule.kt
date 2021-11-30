package di

import codegen.ManualTurningHelper
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import usecase.MessagesUseCase
import usecase.ManualTurningUseCase

val UseCaseModule = DI.Module("UseCase") {
    bindProvider {
        MessagesUseCase(instance())
    }

    bindProvider {
        ManualTurningUseCase(instance("app_scope"), instance(), instance(), instance(), instance())
    }
    bindProvider {
        ManualTurningHelper(instance())
    }
}