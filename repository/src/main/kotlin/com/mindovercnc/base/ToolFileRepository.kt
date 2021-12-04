package com.mindovercnc.base

import com.mindovercnc.base.data.LatheTool
import kotlinx.coroutines.flow.Flow

interface ToolFileRepository {
    fun getTools(): Flow<List<LatheTool>>
    fun addOrUpdateTool(latheTool: LatheTool)
    fun removeTool(toolNo: Int)
}