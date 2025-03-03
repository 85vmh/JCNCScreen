package com.mindovercnc.base

import com.mindovercnc.base.data.CncStatus
import kotlinx.coroutines.flow.Flow

interface CncStatusRepository {
    fun cncStatusFlow() : Flow<CncStatus>
}