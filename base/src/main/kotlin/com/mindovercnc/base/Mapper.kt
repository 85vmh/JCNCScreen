package com.mindovercnc.base

import com.mindovercnc.base.data.PositionState
import com.mindovercnc.base.nml.IBufferDescriptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.nio.ByteBuffer

interface Mapper<T> {
    operator fun invoke(byteBuffer: ByteBuffer): T
}

class PosStateMapper(val descriptor: IBufferDescriptor) : Mapper<PositionState> {
    override fun invoke(byteBuffer: ByteBuffer): PositionState {
        return PositionState(byteBuffer, descriptor)
    }
}

fun <T> Flow<ByteBuffer?>.mapUsing(mapper: Mapper<T>) = map {
    it?.let {
        mapper(it)
    }
}