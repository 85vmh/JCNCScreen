package com.mindovercnc.base

import com.mindovercnc.base.data.TaskMode
import com.mindovercnc.base.data.TaskState

interface CncCommandRepository {

    fun setTaskMode(taskMode: TaskMode)

    fun setTaskState(taskState: TaskState)

    fun taskAbort()

    fun homeAll()

    fun homeAxis(jointNumber : Int)

}