package codegen

import java.util.*

class ConversationalProgram(
    private val programName: String,
    private val creationDate: Date,
    private val operations: List<Operation>
) {

    fun generateGCode(): List<String> {
        val lines = mutableListOf<String>()
        lines.add("(MindOverCNC Lathe - Conversational)")
        lines.add("(Program Name: $programName)")
        lines.add("(Creation Date: $creationDate)")
        lines.add("\r")
        lines.add("G7 G18 G21 (Diameter Mode, XZ Plane, Metric Units)")
        lines.add("\r")
        operations.forEach {
            with(it) {
                lines.addAll(this.getStartComment())
                lines.add("\r")
                lines.addAll(this.getOperationCode())
                lines.add("\r")
                lines.addAll(this.getEndComment())
            }
        }
        lines.add("M2")
        return lines
    }
}