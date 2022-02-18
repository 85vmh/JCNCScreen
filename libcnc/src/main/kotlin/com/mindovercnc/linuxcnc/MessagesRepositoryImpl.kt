package com.mindovercnc.linuxcnc

import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.MessageBundle
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.base.data.UiMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class MessagesRepositoryImpl(
    scope: CoroutineScope
) : MessagesRepository {
    private val errorReader: ErrorReader = ErrorReader()
    private val emcMessages = MutableStateFlow(emptyList<SystemMessage>())
    private val uiMessages = MutableStateFlow(mapOf<UiMessage, Date>())

    init {
        errorReader.refresh(10L)
            .filterNotNull()
            .onEach {
                emcMessages.update { list ->
                    list.plus(it)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(scope)
    }

    override fun messagesFlow(): Flow<MessageBundle> {
        return combine(emcMessages, uiMessages) { emcFlow, uiFlow -> MessageBundle(emcFlow, uiFlow) }
    }

    override fun clearEmcMessages() {
        emcMessages.value = emptyList()
    }

    override fun pushMessage(uiMessage: UiMessage) {
        uiMessages.update {
            it.plus((uiMessage to Date()))
        }
    }

    override fun popMessage(uiMessage: UiMessage) {
        uiMessages.update {
            it.minus(uiMessage)
        }
    }
}