package vtk

import androidx.compose.ui.geometry.Offset

data class Point3D(
    val x: Double,
    val y: Double,
    val z: Double
) {
    fun toDoubleArray(multiplicationFactor: Double = 1.0) = doubleArrayOf(
        x * multiplicationFactor,
        y * multiplicationFactor,
        z * multiplicationFactor
    )

    fun toOffset(multiplicationFactor: Float = 1f) = Offset(
        x = (z * multiplicationFactor).toFloat(),
        y = (x * multiplicationFactor).toFloat()
    )
}
