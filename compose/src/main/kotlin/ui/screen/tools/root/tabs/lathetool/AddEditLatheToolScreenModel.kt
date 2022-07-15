package ui.screen.tools.root.tabs.lathetool

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.model.CuttingInsert
import com.mindovercnc.model.LatheTool
import com.mindovercnc.model.ToolType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import usecase.ToolsUseCase

class AddEditLatheToolScreenModel(
    val latheTool: LatheTool? = null,
    val toolsUseCase: ToolsUseCase
) : StateScreenModel<AddEditLatheToolScreenModel.State>(State()) {

    data class State(
        val latheToolId: Int? = null,
        val toolTypes: List<ToolType> = ToolType.values().toList(),
        val type: ToolType? = null,
        val cuttingInserts: List<CuttingInsert> = emptyList(),
        val cuttingInsert: CuttingInsert? = null
    )

    init {
        latheTool?.let { tool ->
            mutableState.update {
                it.copy(
                    latheToolId = tool.toolId,
                    type = ToolType.fromLatheTool(tool)
                )
            }
        }

        toolsUseCase.getCuttingInserts()
            .onEach { insertsList ->
                mutableState.update {
                    it.copy(
                        cuttingInserts = insertsList,
                    )
                }
            }.launchIn(coroutineScope)
    }

    fun setToolId(value: Int) {
        mutableState.update {
            it.copy(
                latheToolId = value,
            )
        }
    }

    fun setToolType(value: ToolType) {
        mutableState.update {
            it.copy(
                type = value,
            )
        }
    }

    fun applyChanges() {
//        with(mutableState.value) {
//            val th = ToolHolder(
//                holderNumber = this.holderNumber ?: return@with,
//                type = this.type,
//                latheTool = this.latheTool
//            )
//            when (toolHolder) {
//                null -> toolsUseCase.createToolHolder(th)
//                else -> toolsUseCase.updateToolHolder(th)
//            }
//        }
    }
}