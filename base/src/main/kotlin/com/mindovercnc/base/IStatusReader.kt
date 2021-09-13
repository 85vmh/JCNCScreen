package com.mindovercnc.base

import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer

interface IStatusReader {

    fun launch(): Flow<ByteBuffer?>
}