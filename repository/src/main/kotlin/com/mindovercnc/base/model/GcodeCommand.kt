package com.mindovercnc.base.model

data class GcodeCommand(
    val id: Int,
    val name: String,
    val arguments: String
)