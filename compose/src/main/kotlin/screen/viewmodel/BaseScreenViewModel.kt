package screen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import screen.BaseScreen

class BaseScreenViewModel constructor(
    scope: CoroutineScope,
    cncStatusRepository: CncStatusRepository,
    messagesRepository: MessagesRepository,
    startupScreen: BaseScreen
) {
    var screen by mutableStateOf(startupScreen)

    init {
        cncStatusRepository.cncStatusFlow()
            .map { it.isHomed() }
            .distinctUntilChanged()
            .onEach {
                screen = if (it) {
                    messagesRepository.popMessage(UiMessageType.MachineNotHomed)
                    BaseScreen.RootScreen
                } else {
                    messagesRepository.pushMessage(UiMessageType.MachineNotHomed)
                    BaseScreen.NotHomedScreen
                }
            }
            .launchIn(scope)

        cncStatusRepository.cncStatusFlow()
            .map { it.isNotOn }
            .distinctUntilChanged()
            .onEach {
                if (it) {
                    messagesRepository.pushMessage(UiMessageType.MachineNotON)
                } else {
                    messagesRepository.popMessage(UiMessageType.MachineNotON)
                }
            }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isEstop }
            .distinctUntilChanged()
            .onEach {
                if (it) {
                    messagesRepository.pushMessage(UiMessageType.MachineInEstop)
                } else {
                    messagesRepository.popMessage(UiMessageType.MachineInEstop)
                }
            }
            .launchIn(scope)
    }

    fun turningSettingsClicked() {
        screen = BaseScreen.TurningSettingsScreen
    }

    fun turningSettingsClose() {
        screen = BaseScreen.RootScreen
    }
}