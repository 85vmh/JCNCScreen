package usecase

import com.mindovercnc.base.GCodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vtk.*
import java.io.File

data class Vector3f(
    val x: Float,
    val y: Float,
    val z: Float
)

class ICreator3D {
    fun createLine(start: Vector3f, end: Vector3f, line: Int, rapid: Boolean) {
        val type = if (rapid) "Traverse" else "Feed"
        println("--$line $type -> Start: $start, End: $end")
    }

    fun createArc(start: Vector3f, end: Vector3f, center: Vector3f, line: Int, clockwise: Boolean) {
        println("--$line Arc -> Start: $start, End: $end, Center: $center, clockwise: $clockwise")
    }

}


class ActorsUseCase(
    private val gCodeRepository: GCodeRepository
) {

    suspend fun getActors(file: File): List<vtkActor> = withContext(Dispatchers.IO) {
        val pathElements = mutableListOf<PathElement>()
        var lastPoint: Point? = null

        val result = mutableListOf<vtkActor>()

        val creator = ICreator3D()
        var last: Vector3f? = null

        gCodeRepository.parseFile(file).forEach { command ->
            //last = parseLine(command.rawLine, last, creator)

            when (command.name) {
                "STRAIGHT_TRAVERSE",
                "STRAIGHT_FEED" -> {
                    val args = command.arguments.split(", ")
                    val current = Point(
                        x = args[0].toDouble(),
                        y = args[1].toDouble(),
                        z = args[2].toDouble(),
                    )

                    when (lastPoint) {
                        null -> lastPoint = current
                        else -> {
                            pathElements.add(
                                Line(
                                    startPoint = lastPoint!!,
                                    endPoint = current,
                                    type = when (command.name) {
                                        "STRAIGHT_FEED" -> Line.Type.Feed
                                        else -> Line.Type.Traverse
                                    }
                                )
                            )
                            lastPoint = current
                        }
                    }
                }
                "ARC_FEED" -> {
                    /**
                     * ARC_FEED(1.6266,     1.7876,     3.0410,     3.2020,     -1,       0.0000,           0.0000, 0.0000, 0.0000)
                     *          first_end,  second_end, first_axis, second_axis,rotation, axis_end_point,   a,      b,      c
                     */

                    val args = command.arguments.split(", ")
                    val current = Point(
                        x = args[1].toDouble(),
                        y = 0.0,
                        z = args[0].toDouble(),
                    )
                    val center = Point(
                        x = args[3].toDouble(),
                        y = 0.0,
                        z = args[2].toDouble(),
                    )
                    when (lastPoint) {
                        null -> lastPoint = current
                        else -> {
                            pathElements.add(
                                Arc(
                                    startPoint = lastPoint!!,
                                    endPoint = current,
                                    centerPoint = center,
                                    direction = if (args[4].toInt() > 0) Arc.Direction.Clockwise else Arc.Direction.CounterClockwise
                                )
                            )
                            lastPoint = current
                        }
                    }
                }
                else -> {}
            }
        }
        result.add(PathActor(pathElements))

        return@withContext result
    }

    val isYZmapped = true

    private fun parseLine(line: String, lastVector: Vector3f?, primFactory: ICreator3D): Vector3f? {
        println("Line is: $line")
        var last: Vector3f? = lastVector
        if (line.startsWith("READ => ")) return last
        val parts = line.split("\\s+".toRegex(), limit = 4).toTypedArray()
        var lineNum = 0
        if (parts.size < 4) return last
        if (parts[2].startsWith("N")) {
            try {
                lineNum = parts[2].substring(1).toInt()
            } catch (t: Throwable) {
            }
        }
        if (parts[3].contains("(")) {
            val subp = parts[3].split("[(,)]\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (subp.size == 7) {
                //straight line or fast move
                if (subp[0].contains("V")) {
                    // fast move
                    if (last == null) {
                        if (isYZmapped) {
                            last = Vector3f(
                                subp[1].toFloat(), subp[3].toFloat(),
                                -subp[2].toFloat()
                            )
                        } else {
                            last = Vector3f(subp[1].toFloat(), subp[2].toFloat(), subp[3].toFloat())
                        }
                    } else {
                        var next: Vector3f? = null
                        if (isYZmapped) {
                            next = Vector3f(subp[1].toFloat(), subp[3].toFloat(), -subp[2].toFloat())
                        } else {
                            next = Vector3f(subp[1].toFloat(), subp[2].toFloat(), subp[3].toFloat())
                        }
                        //STRAIGHT_TRAVERSE
                        primFactory.createLine(last, next, lineNum, true)
                        last = next
                    }
                } else {
                    if (last == null) {
                        if (isYZmapped) {
                            last = Vector3f(
                                subp[1].toFloat(), subp[3].toFloat(),
                                -subp[2].toFloat()
                            )
                        } else {
                            last = Vector3f(subp[1].toFloat(), subp[2].toFloat(), subp[3].toFloat())
                        }
                    } else {
                        var next: Vector3f? = null
                        if (isYZmapped) {
                            next = Vector3f(
                                subp[1].toFloat(), subp[3].toFloat(),
                                -subp[2].toFloat()
                            )
                        } else {
                            next = Vector3f(subp[1].toFloat(), subp[2].toFloat(), subp[3].toFloat())
                        }
                        //STRAIGHT_FEED
                        primFactory.createLine(last, next, lineNum, false)
                        last = next
                    }
                }
            } else if (subp.size == 10) {
                // arc
                if (last == null) {
                    throw UnsupportedOperationException("can't do arc without start position!")
                } else {
                    var center: Vector3f? = null
                    var next: Vector3f? = null
                    val direction = subp[5].toInt()
                    if (isYZmapped) {
                        next = Vector3f(subp[1].toFloat(), subp[6].toFloat(), -subp[2].toFloat())
                        center = Vector3f(subp[3].toFloat(), subp[6].toFloat(), -subp[4].toFloat())
                    } else {
                        next = Vector3f(subp[1].toFloat(), subp[2].toFloat(), subp[6].toFloat())
                        center = Vector3f(subp[3].toFloat(), subp[4].toFloat(), subp[6].toFloat())
                    }
                    //ARC_FEED
                    primFactory.createArc(last, next, center, lineNum, direction > 0)
                    last = next
                }
            }
        }
        return last
    }
}