package usecase

import codegen.Point
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.Position
import com.mindovercnc.base.data.dtg
import com.mindovercnc.base.data.g53Position
import com.mindovercnc.base.data.getDisplayablePosition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PositionUseCase(
    private val statusRepository: CncStatusRepository
) {
    suspend fun getCurrentPoint() = statusRepository.cncStatusFlow()
        .map { it.getDisplayablePosition() }
        .map { Point(it.x * 2, it.z) } // *2 due to diameter mode
        .first()

    suspend fun getZMachinePosition() = statusRepository.cncStatusFlow()
        .map { it.g53Position }
        .map { it.z }
        .first()

    private fun getDtgPosition(): Flow<Position> {
        return statusRepository.cncStatusFlow()
            .map { it.dtg }
            .distinctUntilChanged()
    }
}