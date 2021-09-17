package com.mindovercnc.base.di

import com.mindovercnc.base.IStatusReader
import com.mindovercnc.base.Initializer
import com.mindovercnc.dummycnc.DummyInitializer
import com.mindovercnc.dummycnc.DummyStatusReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import java.nio.ByteBuffer
import kotlin.random.Random

val DummyModule = DI.Module("dummy") {
    bindSingleton<Initializer> { DummyInitializer }
    bindSingleton<IStatusReader> {
        DummyStatusReader(flow {
            while (true) {
                emit(
                    ByteBuffer.wrap(
                        ByteArray(200000) {
                            Random.nextInt().toByte()
                        }
                    )
                )
                delay(200L)
            }
        }.flowOn(Dispatchers.IO))
    }
}