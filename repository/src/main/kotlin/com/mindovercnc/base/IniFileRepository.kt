package com.mindovercnc.base

import com.mindovercnc.base.data.G53AxisLimits
import com.mindovercnc.base.data.IniFile

interface IniFileRepository {
    fun getIniFile(): IniFile

    fun getActiveLimits(): G53AxisLimits

    fun setCustomG53AxisLimits(g53AxisLimits: G53AxisLimits)

    fun toggleCustomLimits()

    fun isCustomLimitsActive(): Boolean
}