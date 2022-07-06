package usecase.model

import androidx.compose.ui.geometry.Offset

data class Point2D(
    val x: Double,
    val z: Double
) {
    fun toOffset(multiplicationFactor: Float = 1f) = Offset(
        x = (z * multiplicationFactor).toFloat(),
        y = (x * multiplicationFactor).toFloat()
    )

    operator fun plus(point: Point2D): Point2D {
        return Point2D(
            x = x.plus(point.x),
            z = z.plus(point.z)
        )
    }

    operator fun minus(point: Point2D): Point2D {
        return Point2D(
            x = x.minus(point.x),
            z = z.minus(point.z)
        )
    }

    companion object {
        val zero = Point2D(0.0, 0.0)
    }
}
