package com.mindovercnc.base

import com.mindovercnc.base.data.BooleanKey
import com.mindovercnc.base.data.DoubleKey
import com.mindovercnc.base.data.IntegerKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun put(intKey: IntegerKey, value: Int)

    fun put(doubleKey: DoubleKey, value: Double)

    fun put(booleanKey: BooleanKey, value: Boolean)

    fun get(intKey: IntegerKey, defaultValue: Int = 0): Int

    fun get(doubleKey: DoubleKey, defaultValue: Double = 0.0): Double

    fun get(booleanKey: BooleanKey, defaultValue: Boolean = false): Boolean

    fun flow(intKey: IntegerKey, defaultValue: Int = 0): Flow<Int>

    fun flow(doubleKey: DoubleKey, defaultValue: Double = 0.0): Flow<Double>

    fun flow(booleanKey: BooleanKey, defaultValue: Boolean = false): Flow<Boolean>
}