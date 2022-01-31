package com.mindovercnc.linuxcnc

import com.mindovercnc.base.FileSystemRepository
import com.mindovercnc.base.data.AppFile
import kotlinx.coroutines.CoroutineScope
import java.io.File

class FileSystemRepositoryImpl(
    private val scope: CoroutineScope,
    private val ncProgramsPath: String
) : FileSystemRepository {

    override fun getNcRootAppFile(): AppFile {
        return File(ncProgramsPath).toAppFile()
    }
}