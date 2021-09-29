package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.linuxcnc.parsing.CncStatusFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

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
        return combine(
            statusReader.refresh(100L).filterNotNull(),
            errorReader.refresh(100L).filterNotNull()
        ) { byteBuffer, error ->
            cncStatusFactory.parse(byteBuffer)
        }
    }
}