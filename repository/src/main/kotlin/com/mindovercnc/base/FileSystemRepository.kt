package com.mindovercnc.base

import java.io.File

interface FileSystemRepository {
    fun getNcRootAppFile() : File

    fun writeProgramLines(lines: List<String>, programName: String)
}