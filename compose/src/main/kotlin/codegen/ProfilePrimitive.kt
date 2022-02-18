package codegen

import extensions.toFixedDigits

sealed class ProfilePrimitive {

    data class Line(
        val type: Type,
        val xDest: Double? = null,
        val zDest: Double? = null
    ) : ProfilePrimitive() {

        enum class Type(val gCode: String) {
            Traverse("G0"), Feed("G1")
        }

        override fun toString(): String {
            return type.gCode +
                    xDest?.let { " X${it.toFixedDigits()}" } +
                    zDest?.let { " Z${it.toFixedDigits()}" }
        }
    }

    data class Arc(
        val type: Type,
        val xDest: Double? = null,
        val zDest: Double? = null,
        val xOffset: Double,
        val zOffset: Double
    ) : ProfilePrimitive() {

        enum class Type(val gCode: String) {
            CW("G2"), CCW("G3")
        }

        override fun toString(): String {
            return type.gCode +
                    xDest?.let { " X${it.toFixedDigits()}" } +
                    zDest?.let { " Z${it.toFixedDigits()}" } +
                    " I${xOffset.toFixedDigits()}" +
                    " K${zOffset.toFixedDigits()}"
        }
    }
}