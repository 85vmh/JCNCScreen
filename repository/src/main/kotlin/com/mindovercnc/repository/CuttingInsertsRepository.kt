package com.mindovercnc.repository

import com.mindovercnc.model.CuttingInsert

interface CuttingInsertsRepository {
    fun insert(cuttingInsert: CuttingInsert)
    fun findAll(): List<CuttingInsert>
}