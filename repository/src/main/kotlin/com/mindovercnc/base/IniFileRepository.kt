package com.mindovercnc.base

import com.mindovercnc.base.data.IniFile

interface IniFileRepository {
    fun getIniFile(): IniFile
}