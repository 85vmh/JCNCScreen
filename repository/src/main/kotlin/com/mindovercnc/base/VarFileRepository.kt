package com.mindovercnc.base

import com.mindovercnc.base.data.ParametersState
import com.mindovercnc.base.data.Position
import kotlinx.coroutines.flow.Flow

interface VarFileRepository {
    fun getParametersState(): Flow<ParametersState>
}