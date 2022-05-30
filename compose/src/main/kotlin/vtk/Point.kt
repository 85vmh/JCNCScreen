package vtk

data class Point(
    val x: Double,
    val y: Double,
    val z: Double
) {
    fun toDoubleArray(multiplicationFactor: Double = 1.0) = doubleArrayOf(
        x * multiplicationFactor,
        y * multiplicationFactor,
        z * multiplicationFactor
    )
}
