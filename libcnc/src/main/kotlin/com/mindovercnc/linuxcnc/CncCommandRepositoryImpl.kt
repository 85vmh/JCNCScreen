package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CncCommandRepository
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
    
}