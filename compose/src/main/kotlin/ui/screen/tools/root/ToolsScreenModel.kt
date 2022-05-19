package ui.screen.tools.root

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.linuxcnc.model.tools.CuttingInsert
import com.mindovercnc.linuxcnc.model.tools.LatheTool
import com.mindovercnc.linuxcnc.model.tools.ToolHolder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import usecase.ToolsUseCase

class ToolsScreenModel(
    private val toolsUseCase: ToolsUseCase
) : StateScreenModel<ToolsScreenModel.State>(State()) {

    data class State(
        val currentTabIndex: Int = 0,
        val toolHolders: List<ToolHolder> = emptyList(),
        val latheTools: List<LatheTool> = emptyList(),
        val cuttingInserts: List<CuttingInsert> = emptyList(),
        val currentTool: Int = 0,
    )

    init {
        toolsUseCase.getCurrentToolNo()
            .onEach { toolNo ->
                mutableState.update {
                    it.copy(
                        currentTool = toolNo,
                    )
                }
            }.launchIn(coroutineScope)

        toolsUseCase.getToolHolders()
            .onEach { toolList ->
                mutableState.update {
                    it.copy(
                        toolHolders = toolList,
                    )
                }
            }.launchIn(coroutineScope)

        toolsUseCase.getLatheTools()
            .onEach { latheTools ->
                mutableState.update {
                    it.copy(
                        latheTools = latheTools,
                    )
                }
            }.launchIn(coroutineScope)


        toolsUseCase.getCuttingInserts()
            .onEach { insertsList ->
                mutableState.update {
                    it.copy(
                        cuttingInserts = insertsList
                    )
                }
            }.launchIn(coroutineScope)
    }

    fun selectTabWithIndex(tabIndex: Int) {
        mutableState.update {
            it.copy(
                currentTabIndex = tabIndex,
            )
        }
    }


    fun editToolHolder(toolHolder: ToolHolder) {

    }

    fun deleteToolHolder(toolHolder: ToolHolder) {
        toolsUseCase.deleteToolHolder(toolHolder)
    }

    fun loadToolHolder(toolHolder: ToolHolder) {

    }

    fun editCuttingInsert(insert: CuttingInsert) {

    }

    fun deleteCuttingInsert(insert: CuttingInsert) {

    }

    fun deleteLatheTool(latheTool: LatheTool) {
        toolsUseCase.deleteLatheTool(latheTool)
    }
}