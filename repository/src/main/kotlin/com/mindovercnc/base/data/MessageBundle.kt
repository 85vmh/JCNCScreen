package com.mindovercnc.base.data

import java.util.*

data class MessageBundle(
    val emcMessages: List<SystemMessage>,
    val uiMessages: Map<UiMessageType, Date>
)
