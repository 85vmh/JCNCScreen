package com.mindovercnc.base.data.tools

data class ToolHolder(
    val holderNumber: Int,
    val type: ToolHolderType,
    val latheCutter: LatheCutter? = null,
    val xOffset: Double? = null,
    val zOffset: Double? = null,
)