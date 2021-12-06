package usecase

import com.mindovercnc.base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
                    offsets.add(OffsetEntry(getStringRepresentation(index), position.x, position.z))
                }
                offsets
            }
    }

    private fun getStringRepresentation(index: Int): String {
        return when (index) {
            0 -> "G54"
            1 -> "G55"
            2 -> "G56"
            3 -> "G57"
            4 -> "G58"
            5 -> "G59"
            6 -> "G59.1"
            7 -> "G59.2"
            8 -> "G59.3"
            else -> "Gxx"
        }
    }
}