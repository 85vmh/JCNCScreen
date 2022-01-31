package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.TaskMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import usecase.model.OffsetEntry

class OffsetsUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val varFileRepository: VarFileRepository
) {

    fun getOffsets(): Flow<List<OffsetEntry>> {
        return varFileRepository.getParametersState()
            .map {
                val offsets = mutableListOf<OffsetEntry>()
                it.coordinateSystems.forEachIndexed { index, position ->
                    offsets.add(OffsetEntry(getStringRepresentation(index + 1), position.x, position.z))
                }
                offsets
            }
    }

    val currentWcs = statusRepository.cncStatusFlow()
        .map { it.taskStatus.g5xIndex }
        .map { getStringRepresentation(it) }

    fun touchOffX(value: Double) {
        executeMdiCommand("G10 L20 P0 X$value")
    }

    fun touchOffZ(value: Double) {
        executeMdiCommand("G10 L20 P0 Z$value")
    }

    private fun executeMdiCommand(cmd: String) {
        scope.launch {
            val initialTaskMode = statusRepository.cncStatusFlow().map { it.taskStatus.taskMode }.first()
            commandRepository.setTaskMode(TaskMode.TaskModeMDI)
            commandRepository.executeMdiCommand(cmd)
            commandRepository.setTaskMode(initialTaskMode)
        }
    }

    private fun getStringRepresentation(index: Int): String {
        return when (index) {
            0 -> "Current"
            1 -> "G54"
            2 -> "G55"
            3 -> "G56"
            4 -> "G57"
            5 -> "G57"
            6 -> "G58"
            7 -> "G59.1"
            8 -> "G59.2"
            9 -> "G59.3"
            else -> "Gxx"
        }
    }
}