package com.mindovercnc.base

import com.mindovercnc.base.data.CncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CncStatusRepositoryImpl constructor(
    cncInitializer: Initializer,
    val statusReader: IStatusReader,
    val errorReader: IErrorReader
) : CncStatusRepository {

    init {
        cncInitializer.initialize()
    }

    override fun observe(): Flow<CncStatus> {
        return statusReader.refresh(100L)
            .map {
                //do the mapping here and populate the object
                CncStatus()
            }
    }
}