package com.mindovercnc.linuxcnc

import com.mindovercnc.base.GCodeRepository
import java.io.File

class GCodeRepositoryImpl(
    private val iniFilePath: IniFilePath,
    private val toolFilePath: ToolFilePath,
    private val varFilePath: VarFilePath
) : GCodeRepository {

    private val rs274Path = File(LinuxCncHome, "bin/rs274")

    override fun parseFile(file: File) {
        val pb = ProcessBuilder(
            rs274Path.path,
            "-g",
            "-v",
            varFilePath.file.path,
            "-i",
            iniFilePath.file.path,
            "-t",
            toolFilePath.file.path,
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