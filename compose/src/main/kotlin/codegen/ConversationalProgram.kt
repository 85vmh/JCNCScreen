package codegen

import java.util.*

class ConversationalProgram(
    private val programName: String,
    private val creationDate: Date,
    private val operations: List<Operation>
) {

    fun generateGCode(): List<String> {
        val lines = mutableListOf<String>()
        lines.add("MindOverCNC Lathe - Conversational")
        lines.add("Program Name: $programName")
        lines.add("Creation Date: $creationDate")
        lines.add("\n")
        lines.add("G7 G18 G21")

        operations.forEach {
            with(it) {
                lines.addAll(this.getComment())
                lines.addAll(this.getOperationCode())
            }
        }
        lines.add("M2")
        return lines
    }
}