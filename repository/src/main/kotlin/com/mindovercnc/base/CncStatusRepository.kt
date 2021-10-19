package com.mindovercnc.base

import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import kotlinx.coroutines.flow.Flow

interface CncStatusRepository {
    fun cncStatusFlow() : Flow<CncStatus>

    fun errorFlow() : Flow<SystemMessage>
}