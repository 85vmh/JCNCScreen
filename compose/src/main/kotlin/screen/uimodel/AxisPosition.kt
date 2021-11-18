package screen.uimodel

data class AxisPosition(
    val axis: Axis,
    val absValue: Double,
    val relValue: Double? = null,
    val units: Units
) {
    enum class Axis(index: Int) {
        X(0), Z(1)
    }

    enum class Units(val displayDigits: Int) {
        MM(3), IN(4), CM(2)
    }
}
