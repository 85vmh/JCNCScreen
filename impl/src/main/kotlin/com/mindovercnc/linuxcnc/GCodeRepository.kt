package com.mindovercnc.linuxcnc

import com.mindovercnc.base.GCodeRepository
import java.io.File

class GCodeRepositoryImpl(
    private val rs274Path: String,
    private val iniFilePath: String,
    private val toolFilePath: String,
    private val varFilePath: String
) : GCodeRepository {

    override fun parseFile(file: File) {
        val pb = ProcessBuilder(
            rs274Path,
            "-g",
            "-v",
            varFilePath,
            "-i",
            iniFilePath,
            "-t",
            toolFilePath,
            file.absolutePath
        )

        val process = pb.start()
        process.inputReader().useLines {
            it.forEach {
                println("-----Line: $it")
            }
        }
    }
}