package codegen

import com.mindovercnc.base.data.AxisLimits
import extensions.stripZeros
import kotlin.math.abs
import kotlin.math.tan

object ManualTurningHelper {
    enum class Axis(val index: Int) {
        //When jogging, the axes are considered as X=0, Y=1, Z=2
        X(0), Z(2)
    }

    enum class Direction {
        Negative, Positive
    }

    fun getStraightTurningCommand(axis: Axis, feedDirection: Direction, limits: AxisLimits): String {
        val limit = when (axis) {
            Axis.X -> when (feedDirection) {
                Direction.Negative -> limits.xMinLimit!! * 2 //because lathes work in diameter mode
                Direction.Positive -> limits.xMaxLimit!! * 2 //because lathes work in diameter mode
            }
            Axis.Z -> when (feedDirection) {
                Direction.Negative -> limits.zMinLimit
                Direction.Positive -> limits.zMaxLimit
            }
        }
        return "G53 G1 ${axis.name + limit}"
    }

    fun getTaperTurningCommand(axis: Axis, feedDirection: Direction, limits: AxisLimits, startPoint: Point, angle: Double): String {
        val cornerPoint = when (axis) {
            Axis.X -> when (feedDirection) {
                Direction.Positive -> Point(limits.xMaxLimit!! * 2, limits.zMinLimit!!)
                Direction.Negative -> Point(limits.xMinLimit!! * 2, limits.zMaxLimit!!)
            }
            Axis.Z -> when (feedDirection) {
                Direction.Positive -> Point(limits.xMaxLimit!! * 2, limits.zMaxLimit!!)
                Direction.Negative -> Point(limits.xMinLimit!! * 2, limits.zMinLimit!!)
            }
        }

        println("---Start point: $startPoint")
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