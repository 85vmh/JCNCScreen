package usecase.model

data class MachineLimits(
    val xMin: Double = 0.0,
    val xMax: Double = 0.0,
    val zMin: Double = 0.0,
    val zMax: Double = 0.0
) {
    fun toWcsLimits(wcsPosition: Point2D) = WcsLimits(
        xMin = xMin - wcsPosition.x,
        xMax = xMax - wcsPosition.x,
        zMin = zMin - wcsPosition.z,
        zMax = zMax - wcsPosition.z,
    )
}