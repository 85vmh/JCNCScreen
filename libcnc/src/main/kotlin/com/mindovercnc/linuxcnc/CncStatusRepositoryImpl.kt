package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

object CncStatusRepositoryImpl : CncStatusRepository {
    private val statusReader: StatusReader
    private val errorReader: ErrorReader

    init {
        CncInitializer.initialize()
        statusReader = StatusReader()
        errorReader = ErrorReader()
    }

    override fun cncStatusFlow(): Flow<CncStatus> {
        return combine(
            statusReader.refresh(100L).filterNotNull(),
            errorReader.refresh(100L).filterNotNull()
        ) { byteBuffer, error ->
            CncStatusFactory.parse(byteBuffer, statusReader)
        }
    }
}