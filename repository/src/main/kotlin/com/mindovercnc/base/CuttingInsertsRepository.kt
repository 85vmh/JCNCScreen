package com.mindovercnc.base

import com.mindovercnc.linuxcnc.model.tools.CuttingInsert

interface CuttingInsertsRepository {
    fun insert(cuttingInsert: CuttingInsert)
    fun findAll(): List<CuttingInsert>
}