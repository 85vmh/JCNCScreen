package com.mindovercnc.base.data

data class JointsInfo(
    val numJoints: Int = 0,
    val jointsStatuses: List<JointStatus>
) {
    data class JointStatus(val enabled: Boolean = false, val homed: Boolean = false)
}
