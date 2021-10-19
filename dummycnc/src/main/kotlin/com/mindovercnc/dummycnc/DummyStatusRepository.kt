package com.mindovercnc.dummycnc

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.SystemMessage
import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer

class DummyStatusRepository(
    private val flow: Flow<CncStatus>,
    private val systemMessage: Flow<SystemMessage>
) : CncStatusRepository {

    override fun cncStatusFlow(): Flow<CncStatus> {
        return flow
    }

    override fun errorFlow(): Flow<SystemMessage> {
        return systemMessage
    }
}