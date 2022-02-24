package com.mindovercnc.base

import com.mindovercnc.base.data.JoystickPosition
import com.mindovercnc.base.data.JoystickStatus
import com.mindovercnc.base.data.SpindleSwitchStatus
import kotlinx.coroutines.flow.Flow

interface HalRepository {
    fun getJoystickStatus(): Flow<JoystickStatus>

    fun getJoystickPosition(): Flow<JoystickPosition>

    fun getJoystickRapidState(): Flow<Boolean>

    fun setPowerFeedingStatus(isActive: Boolean)

    fun setSpindleStarted(isStarted: Boolean)

    fun getSpindleSwitchStatus(): Flow<SpindleSwitchStatus>

    fun getCycleStartStatus(): Flow<Boolean>

    fun getCycleStopStatus(): Flow<Boolean>

    fun actualSpindleSpeed(): Flow<Float>

    fun jogIncrementValue(): Flow<Float>

    fun getToolChangeToolNumber(): Flow<Int>

    fun getToolChangeRequest(): Flow<Boolean>

    fun setToolChangedResponse()

    fun setAxisLimitXMin(value: Double)

    fun setAxisLimitXMax(value: Double)

    fun setAxisLimitZMin(value: Double)

    fun setAxisLimitZMax(value: Double)
}