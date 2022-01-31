package com.mindovercnc.base

import com.mindovercnc.base.data.JogMode
import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.TaskState

interface CncCommandRepository {

    fun setTaskMode(taskMode: TaskMode)

    fun setTaskState(taskState: TaskState)

    fun taskAbort()

    fun homeAll()

    fun homeAxis(jointNumber: Int)

    fun overrideLimits(jointNumber: Int)

    fun unHomeAll()

    fun unHomeAxis(jointNumber: Int)

    fun setFeedHold(hold: Boolean)

    fun setFeedOverride(double: Double)

    fun pause()

    fun setTeleopEnable(enabled: Boolean)

    fun jogContinuous(jogMode: JogMode = JogMode.AXIS, axisOrJoint: Int, speed: Double)

    fun jogIncremental(jogMode: JogMode = JogMode.AXIS, axisOrJoint: Int, stepSize: Double, speed: Double)

    fun jogAbsolute(jogMode: JogMode = JogMode.AXIS, axisOrJoint: Int, position: Double, speed: Double)

    fun jogStop(jogMode: JogMode = JogMode.AXIS, axisOrJoint: Int)

    /**
     * Warning: this does stop the joint at the specified limit, but it errors out when reaching it,
     * so the machine needs to be re-homed.
     */
    fun setMinPositionLimit(jointNumber: Int, limit: Double)

    /**
     * Warning: this does stop the joint at the specified limit, but it errors out when reaching it,
     * so the machine needs to be re-homed.
     */
    fun setMaxPositionLimit(jointNumber: Int, limit: Double)

    fun setBacklash(jointNumber: Int, backlash: Double)

    fun executeMdiCommand(command: String)

    fun loadProgramFile(filePath: String)
}