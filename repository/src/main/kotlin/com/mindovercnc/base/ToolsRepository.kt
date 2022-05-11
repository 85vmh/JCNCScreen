package com.mindovercnc.base

import com.mindovercnc.base.data.tools.LatheCutter
import com.mindovercnc.base.data.tools.ToolHolder

interface ToolsRepository {

    fun getToolHolders(): List<ToolHolder>

    fun addOrUpdateToolHolder(toolHolder: ToolHolder)

    fun removeToolHolder(toolHolder: ToolHolder)

    fun getLatheCutters(): List<LatheCutter>
    fun addOrUpdateLatheCutter(latheCutter: LatheCutter)
    fun removeLatheCutter(latheCutter: LatheCutter)
}