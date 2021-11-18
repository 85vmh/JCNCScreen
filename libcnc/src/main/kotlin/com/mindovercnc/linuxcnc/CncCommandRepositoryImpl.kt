package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.data.JogMode
import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.TaskState

class CncCommandRepositoryImpl() : CncCommandRepository {
    private val commandWriter = CommandWriter()

    override fun setTaskMode(taskMode: TaskMode) {
        commandWriter.setTaskMode(taskMode.mode)
    }

    override fun setTaskState(taskState: TaskState) {
        commandWriter.setTaskState(taskState.stateNum)
    }

    override fun taskAbort() {
        commandWriter.taskAbort()
    }

    override fun homeAll() {
        homeAxis(-1)
    }

    override fun homeAxis(jointNumber: Int) {
        commandWriter.homeAxis(jointNumber)
    }

    override fun overrideLimits(jointNumber: Int) {
        commandWriter.overrideLimits(jointNumber)
    }

    override fun unHomeAll() {
        unHomeAxis(-1)
    }

    override fun unHomeAxis(jointNumber: Int) {
        commandWriter.unHomeAxis(jointNumber)
    }

    override fun jogContinuous(jogMode: JogMode, axisOrJoint: Int, speed: Double) {
        commandWriter.jogContinuous(jogMode.value, axisOrJoint, speed)
    }

    override fun jogIncremental(jogMode: JogMode, axisOrJoint: Int, stepSize: Double, speed: Double) {
        commandWriter.jogIncremental(jogMode.value, axisOrJoint, stepSize, speed)
    }

    override fun jogAbsolute(jogMode: JogMode, axisOrJoint: Int, position: Double, speed: Double) {
        commandWriter.jogAbsolute(jogMode.value, axisOrJoint, position, speed)
    }

    override fun jogStop(jogMode: JogMode, axisOrJoint: Int) {
        commandWriter.jogStop(jogMode.value, axisOrJoint)
    }

    override fun setMinPositionLimit(jointNumber: Int, limit: Double) {
        commandWriter.setMinPositionLimit(jointNumber, limit)
    }

    override fun setMaxPositionLimit(jointNumber: Int, limit: Double) {
        commandWriter.setMaxPositionLimit(jointNumber, limit)
    }

    override fun setBacklash(jointNumber: Int, backlash: Double) {
        commandWriter.setBacklash(jointNumber, backlash)
    }

    override fun executeMdiCommand(command: String) {
        commandWriter.sendMDICommand(command)
    }
}