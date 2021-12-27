package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.LatheTool
import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.currentToolNo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    fun getCurrentTool(): Flow<LatheTool?> {
        return combine(
            toolFileRepository.getTools().distinctUntilChanged(),
            statusRepository.cncStatusFlow().map { it.currentToolNo }.distinctUntilChanged()
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
}