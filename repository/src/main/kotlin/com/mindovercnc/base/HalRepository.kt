package com.mindovercnc.base

import com.mindovercnc.base.data.JoystickStatus
import com.mindovercnc.base.data.SpindleSwitchStatus
import kotlinx.coroutines.flow.Flow

interface HalRepository {
    fun getJoystickStatus(): Flow<JoystickStatus>

    fun getSpindleSwitchStatus(): Flow<SpindleSwitchStatus>

    fun getCycleStartStatus(): Flow<Boolean>

    fun getCycleStopStatus(): Flow<Boolean>

    fun allowSpindleOperation(allowed: Boolean = true)

    fun actualSpindleSpeed(): Flow<Float>
}