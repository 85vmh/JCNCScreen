package com.mindovercnc.base.data

data class IniFile(
    val subroutinePath: String,
    val programPrefix: String,
    val parameterFile: String,
    val toolTableFile: String,
    val joints: List<JointParameters>
) {

    data class JointParameters(
        val minLimit: Double,
        val maxLimit: Double,
        val home: Double,
        val homeOffset: Double
    )
}