package com.mindovercnc.dummycnc

import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.data.JogMode
import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.TaskState

class DummyCommandRepository : CncCommandRepository {
    override fun setTaskMode(taskMode: TaskMode) {
        
    }

    override fun setTaskState(taskState: TaskState) {
        
    }

    override fun taskAbort() {
        
    }

    override fun homeAll() {
        
    }

    override fun homeAxis(jointNumber: Int) {
        
    }

    override fun overrideLimits(jointNumber: Int) {
        
    }

    override fun unHomeAll() {
        
    }

    override fun unHomeAxis(jointNumber: Int) {
        
    }

    override fun jogContinuous(jogMode: JogMode, axisOrJoint: Int, speed: Double) {
        
    }

    override fun jogIncremental(jogMode: JogMode, axisOrJoint: Int, stepSize: Double, speed: Double) {
        
    }

    override fun jogAbsolute(jogMode: JogMode, axisOrJoint: Int, position: Double, speed: Double) {
        
    }

    override fun jogStop(jogMode: JogMode, axisOrJoint: Int) {
        
    }

    override fun setMinPositionLimit(jointNumber: Int, limit: Double) {
        
    }

    override fun setMaxPositionLimit(jointNumber: Int, limit: Double) {
        
    }

    override fun setBacklash(jointNumber: Int, backlash: Double) {
        
    }
}