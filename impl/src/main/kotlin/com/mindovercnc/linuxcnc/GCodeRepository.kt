package com.mindovercnc.linuxcnc

import com.mindovercnc.base.GCodeRepository
import com.mindovercnc.base.model.GcodeCommand
import com.mindovercnc.linuxcnc.parsing.gcode.GcodeParser
import print.PrintColor
import print.colored
import java.io.File

class GCodeRepositoryImpl(
    private val iniFilePath: IniFilePath,
    private val toolFilePath: ToolFilePath,
    private val varFilePath: VarFilePath
) : GCodeRepository {

    private val rs274Path = File(LinuxCncHome, "bin/rs274")

    override fun parseFile(file: File): List<GcodeCommand> {
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

        println("Reading gcode from file $file")
        println("START GCODE")
        val commands = process.inputReader().useLines {
            it.map { line ->
                println(line.colored(PrintColor.BLUE))
                GcodeParser.parse(line)
            }.toList()
        }
        println("END GCODE")
        return commands
    }
}