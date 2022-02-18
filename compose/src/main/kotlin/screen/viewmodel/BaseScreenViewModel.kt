package screen.viewmodel

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.*
import com.mindovercnc.base.handleMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class BaseScreenViewModel(
    scope: CoroutineScope,
    cncStatusRepository: CncStatusRepository,
    messagesRepository: MessagesRepository
) {

    init {
        cncStatusRepository.cncStatusFlow()
            .map { it.isXHomed.not() }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.XAxisNotHomed) }
            .launchIn(scope)

        cncStatusRepository.cncStatusFlow()
            .map { it.isZHomed.not() }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.ZAxisNotHomed) }
            .launchIn(scope)

        cncStatusRepository.cncStatusFlow()
            .map { it.isXHoming }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.XAxisHoming) }
            .launchIn(scope)

        cncStatusRepository.cncStatusFlow()
            .map { it.isZHoming }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.ZAxisHoming) }
            .launchIn(scope)

        cncStatusRepository.cncStatusFlow()
            .map { it.isNotOn }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.MachineNotON) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isEstop }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.MachineInEstop) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMinSoftLimitOnX }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.ReachedMinSoftLimitX) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMaxSoftLimitOnX }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.ReachedMaxSoftLimitX) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMinSoftLimitOnZ }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.ReachedMinSoftLimitZ) }
            .launchIn(scope)
        cncStatusRepository.cncStatusFlow()
            .map { it.isMaxSoftLimitOnZ }
            .distinctUntilChanged()
            .onEach { messagesRepository.handleMessage(it, UiMessage.ReachedMaxSoftLimitZ) }
            .launchIn(scope)
    }
}