package com.mindovercnc.base

import com.mindovercnc.base.data.AppFile

interface FileSystemRepository {
    fun getNcRootAppFile() : AppFile
}