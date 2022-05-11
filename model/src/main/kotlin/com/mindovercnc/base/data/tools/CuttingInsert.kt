package com.mindovercnc.base.data.tools

data class CuttingInsert(
    val madeOf: MadeOf,
    val code: String,
    val tipRadius: Double,
    val frontAngle: Double,
    val backAngle: Double
) {
    enum class MadeOf {
        Hss, HssCo, Carbide, Cbn, Diamond
    }
}
