package codegen

import extensions.stripZeros

class TurningOperation(
    private val geometryData: TurningProfile,
    private val turningStrategies: List<TurningStrategy>,
    private val startingXPosition: Double,
    private val startingZPosition: Double,
) : Operation {

    override fun getComment(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getOperationCode(): List<String> {
        return mutableListOf<String>().apply {
            addAll(subroutineLines)
            add("\n")
            turningStrategies.forEach {
                when (it) {
                    is TurningStrategy.Roughing -> {
                        add("G40 M6 T${it.toolNumber} G43")
                        add(it.direction.code + it.cutType.prefix + commonParams + it.strategyParams)
                    }
                    is TurningStrategy.Finishing -> {
                        add("G40 M6 T${it.toolNumber} G43")
                        it.feedRate?.let { feed ->
                            add("F${feed.stripZeros()}")
                        }
                        add("G70" + commonParams + it.strategyParams)
                    }
                }
            }
        }
    }

    enum class CutType(val prefix: String) {
        Straight(".1"),
        Pocket(".2")
    }

    enum class TraverseDirection(val code: String) {
        ZAxis("G71"), XAxis("G72")
    }

    sealed class TurningStrategy() {
        abstract val strategyParams: String

        data class Roughing(
            val toolNumber: Int,
            val remainingDistance: Double,
            val cuttingIncrement: Double,
            val retractDistance: Double,
            val cutType: CutType,
            val direction: TraverseDirection
        ) : TurningStrategy() {
            override val strategyParams: String
                get() = " D${remainingDistance.stripZeros()}" +
                        " I${cuttingIncrement.stripZeros()}" +
                        " R${retractDistance.stripZeros()}"
        }

        data class Finishing(
            val toolNumber: Int,
            val startingDistance: Double,
            val endingDistance: Double,
            val numberOfPasses: Int,
            val pathBlendingTolerance: Double = 0.01,
            val feedRate: Double? = null
        ) : TurningStrategy() {
            override val strategyParams: String
                get() = " D${startingDistance.stripZeros()}" +
                        " E${endingDistance.stripZeros()}" +
                        " P$numberOfPasses"
        }
    }

    private val commonParams: String
        get() = " Q${geometryData.subroutineNumber}" +
                " X${startingXPosition.stripZeros()}" +
                " Z${startingZPosition.stripZeros()}"

    private val subroutineLines = mutableListOf<String>().apply {
        add("O${geometryData.subroutineNumber} SUB")
        geometryData.getProfile().forEach {
            add("\t$it")
        }
        add("O${geometryData.subroutineNumber} ENDSUB")
    }
}