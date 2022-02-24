package com.mindovercnc.base

import com.mindovercnc.base.data.AxisLimits
import com.mindovercnc.base.data.IniFile

interface IniFileRepository {
    fun getIniFile(): IniFile

    fun getActiveLimits(): AxisLimits

    fun setCustomAxisLimits(axisLimits: AxisLimits)

    fun toggleCustomLimits()

    fun isCustomLimitsActive(): Boolean
}