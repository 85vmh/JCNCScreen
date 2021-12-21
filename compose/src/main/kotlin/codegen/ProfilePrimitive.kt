package codegen

import extensions.trimDigits

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
                    xDest?.let { " X${it.trimDigits()}" } +
                    zDest?.let { " Z${it.trimDigits()}" }
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
                    xDest?.let { " X${it.trimDigits()}" } +
                    zDest?.let { " Z${it.trimDigits()}" } +
                    " I${xOffset.trimDigits()}" +
                    " K${zOffset.trimDigits()}"
        }
    }
}