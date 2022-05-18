package com.mindovercnc.base

import com.mindovercnc.linuxcnc.model.MessageBundle
import com.mindovercnc.linuxcnc.model.UiMessage
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    fun messagesFlow(): Flow<MessageBundle>

    fun clearEmcMessages()

    fun pushMessage(uiMessage: UiMessage)

    fun popMessage(uiMessage: UiMessage)
}

fun MessagesRepository.handleMessage(isNeeded: Boolean, uiMessage: UiMessage) {
    if (isNeeded) {
        pushMessage(uiMessage)
    } else {
        popMessage(uiMessage)
    }
}