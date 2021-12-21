package codegen

import extensions.stripZeros
import extensions.trimDigits
import kotlin.math.sqrt

class ThreadingOperation(
    private val toolNumber: Int,
    private val spindleSpeed: Int,
    private val startPoint: Point,
    private val threadPitch: Double,
    private val finalZPosition: Double,
    private val threadPeakOffset: Double,
    private val initialCutDepth: Double,
    private val fullThreadDepth: Double = calculateDepth(threadPitch, threadPeakOffset),
    private val threadStarts: Int = 1,
    private val springPasses: Int? = null,
    private val depthDegression: DepthDegression? = null,
    private val compoundSlideAngle: CompoundSlideAngle? = null,
    private val taper: Taper? = null
) : Operation {

    override fun getComment(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getOperationCode(): List<String> {
        val lines = mutableListOf<String>()
        lines.add("G95")
        lines.add("G97 S$spindleSpeed")
        lines.add("M6 T$toolNumber G43")

        val leadOffset = threadPitch / threadStarts
        for (lead in 1..threadStarts) {
            val zOffset = (leadOffset * (lead - 1))
            lines.add("(Cutting thread lead [$lead] of [$threadStarts], zOffset is: $zOffset)")
            lines.add("G0 X${startPoint.x.trimDigits()} Z${(startPoint.z + zOffset).trimDigits()}")
            lines.add(buildThreadingCode(finalZPosition + zOffset))
        }
        return lines
    }

    sealed class Taper(val code: Int, val length: Double) {
        data class AtStart(val start: Double) : Taper(1, start)
        data class AtEnd(val end: Double) : Taper(2, end)
        data class AtBoth(val both: Double) : Taper(3, both)
    }

    sealed class DepthDegression(val value: Double) {
        object ConstantDepth : DepthDegression(1.0)
        object ConstantArea : DepthDegression(2.0)
        data class Custom(val customValue: Double) : DepthDegression(customValue)
    }

    enum class CompoundSlideAngle(val value: Double) {
        Angle290(29.0),
        Angle295(29.5),
        Angle300(30.0)
    }

    private fun buildThreadingCode(finalZPosition: Double): String {
        val builder = StringBuilder()
        builder.append(
            "G76 " +
                    "P${threadPitch.stripZeros()} " +
                    "Z${finalZPosition.stripZeros()} " +
                    "I${threadPeakOffset.stripZeros()} " +
                    "J${initialCutDepth.stripZeros()} " +
                    "K${fullThreadDepth.stripZeros()} "
        )
        if (depthDegression != null) {
            builder.append("R${depthDegression.value.stripZeros()} ")
        }
        if (compoundSlideAngle != null) {
            builder.append("Q${compoundSlideAngle.value.stripZeros()} ")
        }
        if (springPasses != null) {
            builder.append("H$springPasses ")
        }
        if (taper != null) {
            builder.append("L${taper.code} E${taper.length.stripZeros()}")
        }
        return builder.toString()
    }

    companion object {
        fun calculateDepth(threadPitch: Double, threadPeak: Double): Double {
            val triangleHeight = sqrt(3.0) / 2 * threadPitch
            // threadPeak is negative for external threads, positive for internal threads.
            val isExternalThread = threadPeak < 0
            return when {
                isExternalThread -> triangleHeight - (triangleHeight / 4)
                else -> triangleHeight - (triangleHeight / 8)
            }
        }
    }
}