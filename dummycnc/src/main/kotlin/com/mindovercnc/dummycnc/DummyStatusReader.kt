package com.mindovercnc.dummycnc

import com.mindovercnc.base.IStatusReader
import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer

class DummyStatusReader(
    private val flow: Flow<ByteBuffer?>
) : IStatusReader {
    override fun refresh(interval: Long): Flow<ByteBuffer?> {
        return flow
    }
}