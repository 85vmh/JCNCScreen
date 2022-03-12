package codegen

import com.mindovercnc.base.data.G53AxisLimits
import extensions.stripZeros
import kotlin.math.abs
import kotlin.math.tan

object ManualTurningHelper {
    // this constant needs to be added or subtracted based on direction
    // the need for this is caused by the oscillation of the servos, so the position is not fully stable
    // this is only reproducible when the limits are entered with teach-in.
    private const val safetyThreshold = 0.005

    enum class Axis(val index: Int) {
        //When jogging, the axes are considered as X=0, Y=1, Z=2
        X(0), Z(2)
    }

    enum class Direction {
        Negative, Positive
    }

    fun getStraightTurningCommand(axis: Axis, feedDirection: Direction, limits: G53AxisLimits): String {
        println("----G53 Axis Limits: $limits")
        val limit = when (axis) {
            Axis.X -> when (feedDirection) {
                Direction.Negative -> limits.xMinLimit!! * 2 + safetyThreshold //because lathes work in diameter mode
                Direction.Positive -> limits.xMaxLimit!! * 2 - safetyThreshold//because lathes work in diameter mode
            }
            Axis.Z -> when (feedDirection) {
                Direction.Negative -> limits.zMinLimit!! + safetyThreshold
                Direction.Positive -> limits.zMaxLimit!! - safetyThreshold
            }
        }
        return "G53 G1 ${axis.name + limit}"
    }

    fun getTaperTurningCommand(axis: Axis, feedDirection: Direction, limits: G53AxisLimits, startPoint: Point, angle: Double): String {
        println("----G53 Axis Limits: $limits")
        val cornerPoint = when (axis) {
            Axis.X -> when (feedDirection) {
                Direction.Positive -> Point(limits.xMaxLimit!! * 2 - safetyThreshold, limits.zMinLimit!! + safetyThreshold)
                Direction.Negative -> Point(limits.xMinLimit!! * 2 + safetyThreshold, limits.zMaxLimit!! - safetyThreshold)
            }
            Axis.Z -> when (feedDirection) {
                Direction.Positive -> Point(limits.xMaxLimit!! * 2 - safetyThreshold, limits.zMaxLimit!! - safetyThreshold)
                Direction.Negative -> Point(limits.xMinLimit!! * 2 + safetyThreshold, limits.zMinLimit!! + safetyThreshold)
            }
        }

        println("---Start point: $startPoint")
        println("---Corner point: $cornerPoint")
        val destPoint = computeDestinationPoint(startPoint, cornerPoint, angle)
        println("---End point: $destPoint")
        return "G53 G1 X${destPoint.x.stripZeros()} Z${destPoint.z.stripZeros()}"
    }

    private fun computeDestinationPoint(
        startPoint: Point,
        cornerPoint: Point,
        angle: Double
    ): Point {
        val opposite = abs(cornerPoint.x - startPoint.x)
        val adjacent = (opposite / tan(Math.toRadians(angle))) / 2 //divided by 2 due to diameter mode
        val maxDistZ = abs(cornerPoint.z - startPoint.z)
        return if (adjacent > maxDistZ) {
            val extraDistZ = adjacent - maxDistZ
            val sign = if (cornerPoint.x > 0) -1 else 1
            val smallOpposite = extraDistZ * tan(Math.toRadians(angle))
            val destPointX = cornerPoint.x + (2 * smallOpposite * sign) //minus when xMaxLimit, plus when xMinLimit
            Point(destPointX, cornerPoint.z)
        } else {
            val sign = if (cornerPoint.z > 0) 1 else -1
            Point(cornerPoint.x, startPoint.z + (adjacent * sign))
        }
    }
}