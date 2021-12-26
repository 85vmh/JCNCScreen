package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.LatheTool
import com.mindovercnc.base.data.currentToolNo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

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

    fun touchOffX(value: Double) {

    }

    fun touchOffZ(value: Double) {

    }

    fun getCurrentTool(): Flow<LatheTool?> {
        return combine(
            toolFileRepository.getTools().distinctUntilChanged(),
            statusRepository.cncStatusFlow().map { it.currentToolNo }.distinctUntilChanged()
        ) { toolsList, loadedToolNo ->
            toolsList.find { it.toolNo == loadedToolNo }
        }
    }
}