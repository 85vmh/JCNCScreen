package usecase

import com.mindovercnc.repository.*
import com.mindovercnc.linuxcnc.model.*
import com.mindovercnc.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import screen.uimodel.AllowedSpindleDirection
import screen.uimodel.ToolType
import usecase.model.AddEditToolState

class ToolsUseCase(
    scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val messagesRepository: MessagesRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository,
    private val toolsRepository: ToolsRepository,
    private val cuttingInsertsRepository: CuttingInsertsRepository,
    private val varFileRepository: VarFileRepository
) {

    init {
        //TODO: this should be moved somewhere else, and remove the scope from the useCase
        statusRepository.cncStatusFlow()
            .map { it.isHomed() }
            .filter { it }
            .distinctUntilChanged()
            .onEach {
                val lastTool = settingsRepository.get(IntegerKey.LastToolUsed)
                if (lastTool != 0) {
                    loadTool(lastTool)
                }
            }
            .launchIn(scope)
    }

    fun getToolHolders(): Flow<List<ToolHolder>> {
        return flowOf(toolsRepository.getToolHolders())
    }

    fun createToolHolder(toolHolder: ToolHolder) {
        toolsRepository.createToolHolder(toolHolder)
    }

    fun updateToolHolder(toolHolder: ToolHolder) {
        toolsRepository.updateToolHolder(toolHolder)
    }

    fun getLatheTools(): Flow<List<LatheTool>> {
        return flowOf(toolsRepository.getLatheTools())
    }

    fun getCuttingInserts(): Flow<List<CuttingInsert>> {
        return flowOf(cuttingInsertsRepository.findAll())
    }

    fun deleteToolHolder(toolHolder: ToolHolder){
        toolsRepository.deleteToolHolder(toolHolder)
    }

    fun deleteLatheTool(tool: LatheTool) {
        toolsRepository.deleteLatheTool(tool)
    }

    fun getTools(): Flow<List<LatheTool>> {
        //return toolsRepository.getTools()
        return flowOf(emptyList())
    }

    suspend fun toolTouchOffX(value: Double) {
        toolTouchOff("X$value")
    }

    suspend fun toolTouchOffZ(value: Double) {
        toolTouchOff("Z$value")
    }

    suspend fun loadTool(toolNo: Int) {
        val initialTaskMode = statusRepository.cncStatusFlow().map { it.taskStatus.taskMode }.first()
        commandRepository.setTaskMode(TaskMode.TaskModeMDI)
        commandRepository.executeMdiCommand("M61 Q$toolNo G43")
        delay(200)
        commandRepository.setTaskMode(initialTaskMode)
    }

    fun deleteTool(toolNo: Int) {
        //toolsRepository.removeTool(toolNo)
    }

    fun getCurrentToolNo(): Flow<Int> {
        return statusRepository.cncStatusFlow()
            .map { it.currentToolNo }
            .distinctUntilChanged()
            .onEach { settingsRepository.put(IntegerKey.LastToolUsed, it) }
    }

//    fun getCurrentTool(): Flow<LatheTool?> {
//        return combine(
//            toolsRepository.getTools().distinctUntilChanged(),
//            getCurrentToolNo()
//        ) { toolsList, loadedToolNo ->
//            toolsList.find { it.toolNo == loadedToolNo }
//        }
//    }

    private suspend fun toolTouchOff(axisWithValue: String) {
        val initialTaskMode = statusRepository.cncStatusFlow().map { it.taskStatus.taskMode }.first()
        val currentTool = statusRepository.cncStatusFlow().map { it.currentToolNo }.first()
        commandRepository.setTaskMode(TaskMode.TaskModeMDI)
        commandRepository.executeMdiCommand("G10 L10 P$currentTool $axisWithValue")
        //TODO: make this based on status channel
        delay(200)
        commandRepository.executeMdiCommand("G43")
        delay(200)
        commandRepository.setTaskMode(initialTaskMode)
        commandRepository.setTeleopEnable(true)
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
/**
 * Ce vreau de la tool:
 * - Sa pot defini insert si regimuri de aschiere per insert.
 * - Cutitul are regim de aschiere
 * - Sa am mai multe cuttere decat holdere
 * - Sa pot sa definesc un max tool length.
 *
 * Insert: tip radius, viteza radiala, depth of cut, feed
 * LatheCutter: Id, Insert, Orientation
 * ToolHolder: Id, LatheCutterId?, Stickout
 *
 * Tool: Id = ToolHolderId
 *
 *
 */