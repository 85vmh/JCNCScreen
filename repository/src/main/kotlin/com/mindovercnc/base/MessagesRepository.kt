package com.mindovercnc.base

import com.mindovercnc.base.data.MessageBundle
import com.mindovercnc.base.data.UiMessageType
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    fun messagesFlow(): Flow<MessageBundle>

    fun clearEmcMessages()

    fun pushMessage(uiMessageType: UiMessageType)

    fun popMessage(uiMessageType: UiMessageType)
}

fun MessagesRepository.handleMessage(isNeeded: Boolean, uiMessageType: UiMessageType) {
    if (isNeeded) {
        pushMessage(uiMessageType)
    } else {
        popMessage(uiMessageType)
    }
}