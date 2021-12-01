package codegen

import com.mindovercnc.base.IniFileRepository
import extensions.stripZeros
import kotlin.math.abs
import kotlin.math.tan

class ManualTurningHelper(
    iniFileRepository: IniFileRepository
) {
    private val xAxisParam = iniFileRepository.getIniFile().joints[0]
    private val zAxisParam = iniFileRepository.getIniFile().joints[1]

    enum class Axis(val index: Int) {
        //When jogging, the axes are considered as X=0, Y=1, Z=2
        X(0), Z(2)
    }

    enum class Direction {
        Negative, Positive
    }

    fun getStraightTurningCommand(axis: Axis, feedDirection: Direction): String {
        val limit = when (axis) {
            Axis.X -> when (feedDirection) {
                Direction.Negative -> xAxisParam.minLimit * 2 //because lathes work in diameter mode
                Direction.Positive -> xAxisParam.maxLimit * 2 //because lathes work in diameter mode
            }
            Axis.Z -> when (feedDirection) {
                Direction.Negative -> zAxisParam.minLimit
                Direction.Positive -> zAxisParam.maxLimit
            }
        }
        return "G53 G1 ${axis.name + limit}"
    }

    fun getTaperTurningCommand(axis: Axis, feedDirection: Direction, startPoint: Point, angle: Double): String {
        val cornerPoint = when (axis) {
            Axis.X -> when (feedDirection) {
                Direction.Positive -> Point(xAxisParam.maxLimit * 2, zAxisParam.minLimit)
                Direction.Negative -> Point(xAxisParam.minLimit * 2, zAxisParam.maxLimit)
            }
            Axis.Z -> when (feedDirection) {
                Direction.Positive -> Point(xAxisParam.maxLimit * 2, zAxisParam.maxLimit)
                Direction.Negative -> Point(xAxisParam.minLimit * 2, zAxisParam.minLimit)
            }
        }

        println("start point: $startPoint")
        val destPoint = computeDestinationPoint(startPoint, cornerPoint, angle)
        println("dest point: $destPoint")
        return "G53 G1 X${destPoint.x.stripZeros()} Z${destPoint.z.stripZeros()}"
    }

    private fun computeDestinationPoint(
        startPoint: Point,
        cornerPoint: Point,
        angle: Double
    ): Point {
        val opposite = abs(cornerPoint.x - startPoint.x)
        val adjacent = opposite / tan(Math.toRadians(angle))
        val maxDistZ = abs(cornerPoint.z - startPoint.z)
        return if (adjacent > maxDistZ) {
            val extraDistZ = adjacent - maxDistZ
            val sign = if (cornerPoint.x > 0) -1 else 1
            val smallOpposite = extraDistZ * tan(Math.toRadians(angle))
            val destPointX = cornerPoint.x + (smallOpposite * sign) //minus when xMaxLimit, plus when xMinLimit
            Point(destPointX, cornerPoint.z)
        } else {
            val sign = if (cornerPoint.z > 0) 1 else -1
            Point(cornerPoint.x, startPoint.z + (adjacent * sign))
        }
    }
}