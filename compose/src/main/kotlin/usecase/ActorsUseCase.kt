package usecase

import com.mindovercnc.base.GCodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vtk.*
import java.io.File

class ActorsUseCase(
    private val gCodeRepository: GCodeRepository
) {

    suspend fun getActors(file: File): List<vtkActor> = withContext(Dispatchers.IO) {
        var last = Point(0.0, 0.0, 0.0)

        gCodeRepository.parseFile(file).mapNotNull {
            when (it.name) {
                "STRAIGHT_FEED" -> {
                    val args = it.arguments.split(", ")
                    val current = Point(
                        x = args[0].toDouble(),
                        y = args[1].toDouble(),
                        z = args[2].toDouble(),
                    )
                    Line3DActor(last, current).apply {
                        last = current
                    }
                }
                "STRAIGHT_TRAVERSE" -> {
                    val args = it.arguments.split(", ")
                    val current = Point(
                        x = args[0].toDouble(),
                        y = args[1].toDouble(),
                        z = args[2].toDouble(),
                    )
                    Line3DActor(last, current).apply {
                        last = current
                    }
                }
                "ARC_FEED" -> null
                else -> null
            }
        }
    }
}