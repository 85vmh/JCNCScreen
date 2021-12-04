package com.mindovercnc.base.data

data class LatheTool(
    val toolNo: Int,
    val pocket: Int = toolNo,
    val orientation: Orientation,
    val xOffset: Double,
    val zOffset: Double,
    val frontAngle: Double,
    val backAngle: Double,
    val tipRadius: Double,
    val type: Type? = null,
    val comment: String
) {

    enum class Type {
        Profiling,
        Parting,
        Drill,
        Reamer
    }

    fun toFormattedString(): String {
        return "T$toolNo P$pocket X$xOffset Z$zOffset D$tipRadius I$frontAngle J$backAngle Q${orientation.orient} ;$comment"
    }

    enum class Orientation(val orient: Int, val angle: Int) {
        Position1(1, 135),
        Position2(2, 45),
        Position3(3, 315),
        Position4(4, 225),
        Position5(5, 180),
        Position6(6, 90),
        Position7(7, 0),
        Position8(8, 270);

        companion object {
            fun getOrientation(orient: Int): Orientation? {
                return values().find { it.orient == orient }
            }
        }
    }

    class Builder {
        var toolNo: Int = 0
        var orientation: Orientation = Orientation.Position2
        var xOffset = 0.0
        var zOffset = 0.0
        var frontAngle = 0.0
        var backAngle = 0.0
        var tipRadius = 0.0
        var type = Type.Profiling
        var comment = ""

        fun build() = LatheTool(
            toolNo = toolNo,
            orientation = orientation,
            xOffset = xOffset,
            zOffset = zOffset,
            frontAngle = frontAngle,
            backAngle = backAngle,
            tipRadius = tipRadius,
            type = type,
            comment = comment
        )
    }
}
