package codegen

import extensions.trimDigits

class DrillingOperation(
    private val toolNumber: Int,
    private val spindleSpeed: Int,
    private val feedRate: Double,
    private val zStartPosition: Double,
    private val zEndPosition: Double,
    private val increment: Double,
    private val repeat: Int? = null
) : Operation {

    override fun getComment() = mutableListOf<String>()
        .apply {
            add("(Drilling operation)")
        }


    override fun getOperationCode() = mutableListOf<String>()
        .apply {
            add("G95 F${feedRate.trimDigits()}")
            add("G97 S$spindleSpeed")

            add("M6 T$toolNumber G43")
            add("G0 Z${zStartPosition.trimDigits()}")
            add("G0 X0") //when drilling always move to x=0
            add(
                "G73 " +
                        "Z${zEndPosition.trimDigits()} " +
                        "R${zStartPosition.trimDigits()} " +
                        "Q$increment " +
                        if (repeat != null) "L$repeat" else ""
            )
        }
}