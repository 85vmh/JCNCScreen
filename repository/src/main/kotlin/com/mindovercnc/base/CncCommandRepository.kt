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

    fun jogContinuous(jogMode: JogMode = JogMode.JOINT, axisOrJoint: Int, speed: Double)

    fun jogIncremental(jogMode: JogMode = JogMode.JOINT, axisOrJoint: Int, stepSize: Double, speed: Double)

    fun jogAbsolute(jogMode: JogMode = JogMode.JOINT, axisOrJoint: Int, position: Double, speed: Double)

    fun jogStop(jogMode: JogMode = JogMode.JOINT, axisOrJoint: Int)

    fun setMinPositionLimit(jointNumber: Int, limit: Double)

    fun setMaxPositionLimit(jointNumber: Int, limit: Double)

    fun setBacklash(jointNumber: Int, backlash: Double)

    fun executeMdiCommand(command: String)
}