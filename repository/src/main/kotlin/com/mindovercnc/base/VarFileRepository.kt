package com.mindovercnc.base

import com.mindovercnc.linuxcnc.model.ParametersState
import com.mindovercnc.linuxcnc.model.Position
import kotlinx.coroutines.flow.Flow

interface VarFileRepository {
    fun getParametersState(): Flow<ParametersState>
}