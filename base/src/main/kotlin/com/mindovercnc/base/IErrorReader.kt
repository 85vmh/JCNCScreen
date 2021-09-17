package com.mindovercnc.base

import com.mindovercnc.base.nml.SystemMessage
import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer

interface IErrorReader {
    fun refresh(interval: Long): Flow<SystemMessage?>
}