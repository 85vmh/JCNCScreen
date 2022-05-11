package com.mindovercnc.base

import com.mindovercnc.base.data.tools.CuttingInsert

interface CuttingInsertsRepository {
    fun insert(cuttingInsert: CuttingInsert)
    fun findAll(): List<CuttingInsert>
}