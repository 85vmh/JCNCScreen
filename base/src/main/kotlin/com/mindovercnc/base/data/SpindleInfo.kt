package com.mindovercnc.base.data

data class SpindleInfo(
    val spindlesCount: Int = 1,
    val spindles: List<Spindle>
) {
    data class Spindle(
        val spindleDirection: SpindleDirection? = null,
        val spindleOverride: Double = 0.0,
        val spindleCurrentSpeed: Double = 0.0,
        val spindleNominalSpeed: Double = 0.0,
        val spindleEnabled: Boolean,
        val spindleHomed: Boolean,
    ) {
        enum class SpindleDirection(value: Int) {
            FORWARD(1),
            REVERSE(2),
        }
    }
}
