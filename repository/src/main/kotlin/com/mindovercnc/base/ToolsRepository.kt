package com.mindovercnc.base

import com.mindovercnc.linuxcnc.model.tools.LatheTool
import com.mindovercnc.linuxcnc.model.tools.ToolHolder

interface ToolsRepository {

    fun getToolHolders(): List<ToolHolder>

    fun createToolHolder(toolHolder: ToolHolder)
    fun updateToolHolder(toolHolder: ToolHolder)

    fun deleteToolHolder(toolHolder: ToolHolder): Boolean

    fun getLatheTools(): List<LatheTool>
    fun createLatheCutter(latheTool: LatheTool)
    fun updateLatheCutter(latheTool: LatheTool)
    fun deleteLatheTool(latheTool: LatheTool): Boolean
}