package screen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.*
import com.mindovercnc.base.handleMessage
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
                    BaseScreen.HomedTabbedScreen
                } else {
                    BaseScreen.NotHomedScreen
                }
            }
            .launchIn(scope)

        cncStatusRepository.cncStatusFlow()
            .map { it.isNotOn }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessageType.MachineNotON) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isEstop }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessageType.MachineInEstop) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMinSoftLimitOnX }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessageType.ReachedMinSoftLimitX) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMaxSoftLimitOnX }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessageType.ReachedMaxSoftLimitX) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMinSoftLimitOnZ }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessageType.ReachedMinSoftLimitZ) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMaxSoftLimitOnZ }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessageType.ReachedMaxSoftLimitZ) }
            .launchIn(scope)
    }
}