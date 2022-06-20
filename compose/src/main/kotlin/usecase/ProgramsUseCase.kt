package usecase

import com.mindovercnc.repository.CncCommandRepository
import com.mindovercnc.repository.CncStatusRepository
import com.mindovercnc.linuxcnc.model.Position
import com.mindovercnc.model.dtg
import com.mindovercnc.model.getDisplayablePosition
import kotlinx.coroutines.flow.*
import screen.uimodel.AxisPosition
import screen.uimodel.PositionModel

class ProgramsUseCase(
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
) {

    private fun getG5xPosition(): Flow<Position> {
        return statusRepository.cncStatusFlow()
            .map { it.getDisplayablePosition() }
            .distinctUntilChanged()
    }

    private fun getDtgPosition(): Flow<Position> {
        return statusRepository.cncStatusFlow()
            .map { it.dtg }
            .distinctUntilChanged()
    }

    val uiModel = combine(
        getG5xPosition(),
        getDtgPosition(),
    ) { g5xPos, dtgPos ->
        val xAxisPos = AxisPosition(AxisPosition.Axis.X, g5xPos.x, dtgPos.x, units = AxisPosition.Units.MM)
        val zAxisPos = AxisPosition(AxisPosition.Axis.Z, g5xPos.z, dtgPos.z, units = AxisPosition.Units.MM)
        PositionModel(xAxisPos, zAxisPos, true)
    }

//    fun loadSelectedProgram(): Boolean {
//        currentFile.value?.let {
//            println("Loading program file: ${it.path}")
//            commandRepository.loadProgramFile(it.path)
//        }
//        return true
//    }

    fun runProgram(){
        commandRepository.runProgram()
    }

    fun stopProgram(){
        commandRepository.stopProgram()
    }
}
