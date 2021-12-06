package di

import codegen.ManualTurningHelper
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import usecase.ManualTurningUseCase
import usecase.MessagesUseCase
import usecase.OffsetsUseCase
import usecase.ToolsUseCase

val UseCaseModule = DI.Module("UseCase") {
    bindSingleton {
        MessagesUseCase(messagesRepository = instance())
    }

    bindSingleton {
        ManualTurningUseCase(
            scope = instance(tag = "app_scope"),
            statusRepository = instance(),
            commandRepository = instance(),
            messagesRepository = instance(),
            halRepository = instance(),
            manualTurningHelper = instance(),
            settingsRepository = instance()
        )
    }

    bindSingleton {
        ToolsUseCase(
            scope = instance(tag = "app_scope"),
            statusRepository = instance(),
            commandRepository = instance(),
            messagesRepository = instance(),
            halRepository = instance(),
            settingsRepository = instance(),
            toolFileRepository = instance(),
            varFileRepository = instance()
        )
    }

    bindSingleton {
        OffsetsUseCase(
            scope = instance(tag = "app_scope"),
            statusRepository = instance(),
            commandRepository = instance(),
            varFileRepository = instance()
        )
    }

    bindSingleton {
        ManualTurningHelper(iniFileRepository = instance())
    }
}