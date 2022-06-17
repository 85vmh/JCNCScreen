package com.mindovercnc.model

data class CuttingInsert(
    val id: Int? = null,
    val madeOf: MadeOf,
    val code: String,
    val tipRadius: Double,
    val tipAngle: Double,
)
