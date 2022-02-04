package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.LatheTool
import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.currentToolNo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import screen.uimodel.AllowedSpindleDirection
import screen.uimodel.ToolType
import usecase.model.AddEditToolState

class ToolsUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val messagesRepository: MessagesRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository,
    private val toolFileRepository: ToolFileRepository,
    private val varFileRepository: VarFileRepository
) {

    fun getTools(): Flow<List<LatheTool>> {
        return toolFileRepository.getTools()
    }

    fun toolTouchOffX(value: Double) {
        toolTouchOff("X$value")
    }

    fun toolTouchOffZ(value: Double) {
        toolTouchOff("Z$value")
    }

    fun loadTool(toolNo: Int) {
        scope.launch {
            val initialTaskMode = statusRepository.cncStatusFlow().map { it.taskStatus.taskMode }.first()
            commandRepository.setTaskMode(TaskMode.TaskModeMDI)
            commandRepository.executeMdiCommand("M61 Q$toolNo G43")
            commandRepository.setTaskMode(initialTaskMode)
        }
    }

    fun deleteTool(toolNo: Int) {
        toolFileRepository.removeTool(toolNo)
    }

    fun getCurrentToolNo(): Flow<Int> {
        return statusRepository.cncStatusFlow().map { it.currentToolNo }.distinctUntilChanged()
    }

    fun getCurrentTool(): Flow<LatheTool?> {
        return combine(
            toolFileRepository.getTools().distinctUntilChanged(),
            getCurrentToolNo()
        ) { toolsList, loadedToolNo ->
            toolsList.find { it.toolNo == loadedToolNo }
        }
    }

    private fun toolTouchOff(axisWithValue: String) {
        scope.launch {
            val initialTaskMode = statusRepository.cncStatusFlow().map { it.taskStatus.taskMode }.first()
            val currentTool = statusRepository.cncStatusFlow().map { it.currentToolNo }
            commandRepository.setTaskMode(TaskMode.TaskModeMDI)
            commandRepository.executeMdiCommand("G10 L10 P$currentTool $axisWithValue")
            commandRepository.executeMdiCommand("G43")
            commandRepository.setTaskMode(initialTaskMode)
        }
    }

    val toolState = AddEditToolState(
        toolNo = 1,
        toolType = ToolType.PROFILING,
        spindleDirection = AllowedSpindleDirection.CCW,
        xOffset = 0.0,
        zOffset = 0.0,
        tipRadius = 0.1,
        diameter = 6.0,
        bladeWidth = 0.0,
        orientation = 1
    )
}