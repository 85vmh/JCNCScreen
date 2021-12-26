package di

import codegen.ManualTurningHelper
import navigation.AppNavigator
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import usecase.*

val UseCaseModule = DI.Module("UseCase") {
    bindSingleton {
        MessagesUseCase(messagesRepository = instance())
    }

    bindSingleton { AppNavigator() }

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
        ManualPositionUseCase(
            scope = instance(tag = "app_scope"),
            cncStatusRepository = instance(),
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
        ConversationalUseCase(
            scope = instance(tag = "app_scope"),
            statusRepository = instance(),
            commandRepository = instance(),
            settingsRepository = instance()
        )
    }

    bindSingleton {
        ProgramsUseCase(
            scope = instance(tag = "app_scope"),
            statusRepository = instance(),
            commandRepository = instance(),
            settingsRepository = instance()
        )
    }


    bindSingleton {
        ManualTurningHelper(iniFileRepository = instance())
    }
}