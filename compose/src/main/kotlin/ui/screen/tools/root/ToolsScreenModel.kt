package ui.screen.tools.root

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.base.data.tools.ToolHolder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import usecase.ToolsUseCase

class ToolsScreenModel(
    private val toolsUseCase: ToolsUseCase
) : StateScreenModel<ToolsScreenModel.State>(State()) {

    data class State(
        val toolHolders: List<ToolHolder> = emptyList(),
        val currentTool: Int = 0,
    )

    init {
        toolsUseCase.getToolHolders()
            .onEach { toolList ->
                mutableState.update {
                    it.copy(
                        toolHolders = toolList,
                    )
                }
            }.launchIn(coroutineScope)

        toolsUseCase.getCurrentToolNo()
            .onEach { toolNo ->
                mutableState.update {
                    it.copy(
                        currentTool = toolNo,
                    )
                }
            }.launchIn(coroutineScope)
    }


    fun editToolHolder(toolHolder: ToolHolder){

    }

    fun deleteToolHolder(toolHolder: ToolHolder){

    }

    fun loadToolHolder(toolHolder: ToolHolder){

    }
}