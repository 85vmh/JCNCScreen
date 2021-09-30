package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.linuxcnc.parsing.CncStatusFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class CncStatusRepositoryImpl constructor(
    private val cncStatusFactory: CncStatusFactory
) : CncStatusRepository {
    private val statusReader: StatusReader
    private val errorReader: ErrorReader

    init {
        CncInitializer.initialize()
        statusReader = StatusReader()
        errorReader = ErrorReader()
    }

    override fun cncStatusFlow(): Flow<CncStatus> {
        return statusReader.refresh(100L)
            .filterNotNull()
            .map { cncStatusFactory.parse(it) }
    }

    override fun errorFlow(): Flow<SystemMessage> {
        return errorReader.refresh(10L).filterNotNull()
    }
}