package com.mindovercnc.linuxcnc.parsing.gcode

import com.mindovercnc.base.model.GcodeCommand
import com.mindovercnc.log.PrintColor
import com.mindovercnc.log.colored

object GcodeParser {

    fun parse(line: String): GcodeCommand {
        val id = line.substringBefore("N..... ").trim().toInt()
        println("\t\tId:\t\t\t\t$id".colored(PrintColor.YELLOW))

        val command = line.substringAfter("N..... ")
        val splitPos = command.indexOf('(')

        val commandName = command.substring(startIndex = 0, endIndex = splitPos)
        println("\t\tCommandName:\t$commandName".colored(PrintColor.YELLOW))

        val arguments = command.substring(splitPos + 1, command.lastIndex)
        if (arguments.isNotEmpty()) {
            println("\t\tArguments:\t\t${arguments}".colored(PrintColor.YELLOW))
        }

        return GcodeCommand(id, commandName, arguments)
    }

}