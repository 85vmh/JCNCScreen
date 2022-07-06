package usecase

import com.mindovercnc.repository.GCodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import usecase.model.PathElement
import vtk.*
import java.io.File

class GCodeUseCase(
    private val gCodeRepository: GCodeRepository
) {

    suspend fun getPathElements(file: File): List<PathElement> = withContext(Dispatchers.IO) {
        val pathElements = mutableListOf<PathElement>()
        var lastPoint: Point3D? = null

        gCodeRepository.parseFile(file).forEach { command ->
            when (command.name) {
                "STRAIGHT_TRAVERSE",
                "STRAIGHT_FEED" -> {
                    val args = command.arguments.split(", ")
                    val current = Point3D(
                        x = args[0].toDouble(),
                        y = args[1].toDouble(),
                        z = args[2].toDouble(),
                    )

                    when (lastPoint) {
                        null -> lastPoint = current
                        else -> {
                            pathElements.add(
                                PathElement.Line(
                                    startPoint = lastPoint!!,
                                    endPoint = current,
                                    type = when (command.name) {
                                        "STRAIGHT_FEED" -> PathElement.Line.Type.Feed
                                        else -> PathElement.Line.Type.Traverse
                                    }
                                )
                            )
                            lastPoint = current
                        }
                    }
                }
                "ARC_FEED" -> {
                    val args = command.arguments.split(", ")
                    val current = Point3D(
                        x = args[1].toDouble(),
                        y = 0.0,
                        z = args[0].toDouble(),
                    )
                    val center = Point3D(
                        x = args[3].toDouble(),
                        y = 0.0,
                        z = args[2].toDouble(),
                    )
                    when (lastPoint) {
                        null -> lastPoint = current
                        else -> {
                            pathElements.add(
                                PathElement.Arc(
                                    startPoint = lastPoint!!,
                                    endPoint = current,
                                    centerPoint = center,
                                    direction = if (args[4].toInt() > 0) PathElement.Arc.Direction.Clockwise else PathElement.Arc.Direction.CounterClockwise
                                )
                            )
                            lastPoint = current
                        }
                    }
                }
                else -> {}
            }
        }
        pathElements
    }
}