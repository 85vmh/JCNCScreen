package com.mindovercnc.dummycnc

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer

class DummyStatusRepository(
    private val flow: Flow<CncStatus>
) : CncStatusRepository {

    override fun cncStatusFlow(): Flow<CncStatus> {
        return flow
    }
}