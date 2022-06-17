package com.mindovercnc.repository

import com.mindovercnc.model.LatheTool
import com.mindovercnc.model.ToolHolder

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