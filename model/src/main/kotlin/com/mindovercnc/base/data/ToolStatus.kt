package com.mindovercnc.base.data

data class ToolStatus(
    val pocketPrepared: Int,
    val currentLoadedTool: Int = 0
)
