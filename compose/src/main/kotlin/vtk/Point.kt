package vtk

data class Point(
    val x: Double,
    val y: Double,
    val z: Double
) {
    fun toDoubleArray() = doubleArrayOf(x, y, z)
}
