package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import com.mindovercnc.linuxcnc.parsing.CncStatusFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class CncStatusRepositoryImpl constructor(
    private val scope: CoroutineScope,
    private val cncStatusFactory: CncStatusFactory
) : CncStatusRepository {
    private val statusReader: StatusReader = StatusReader()
    private val errorReader: ErrorReader = ErrorReader()
    private val statusFlow = statusReader.refresh(100L)
        .filterNotNull()
        .map { cncStatusFactory.parse(it) }
        .shareIn(scope, SharingStarted.Eagerly, replay = 1)


    override fun cncStatusFlow(): Flow<CncStatus> {
        return statusFlow
    }

    override fun errorFlow(): Flow<SystemMessage> {
        return errorReader.refresh(10L).filterNotNull()
    }
}