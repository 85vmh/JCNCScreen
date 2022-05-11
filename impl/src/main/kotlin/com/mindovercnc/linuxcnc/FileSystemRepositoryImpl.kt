package com.mindovercnc.linuxcnc

import com.mindovercnc.base.FileSystemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileSystemRepositoryImpl(
    private val scope: CoroutineScope,
    private val ncProgramsPath: String
) : FileSystemRepository {

    override fun getNcRootAppFile(): File {
        return File(ncProgramsPath)
    }

    override fun writeProgramLines(lines: List<String>, programName: String) {
        scope.launch(Dispatchers.IO) {
            val conversationalFolder = File("$ncProgramsPath/conversational")
            if (conversationalFolder.exists().not()) {
                conversationalFolder.mkdir()
            }
            val programFile = File(conversationalFolder, programName)
            programFile.printWriter().use {
                lines.forEach { line -> it.println(line) }
                it.close()
            }
        }
    }
}