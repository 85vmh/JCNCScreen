package com.mindovercnc.linuxcnc

import com.mindovercnc.repository.CuttingInsertsRepository
import com.mindovercnc.model.CuttingInsert
import com.mindovercnc.database.entity.CuttingInsertEntity
import org.jetbrains.exposed.sql.transactions.transaction

class CuttingInsertsRepositoryImpl : CuttingInsertsRepository {

    override fun insert(cuttingInsert: CuttingInsert) {
        CuttingInsertEntity.new {
            madeOf = cuttingInsert.madeOf
            code = cuttingInsert.code
            tipRadius = cuttingInsert.tipRadius
            tipAngle = cuttingInsert.tipAngle
        }
    }

    override fun findAll(): List<CuttingInsert> {
        return transaction {
            CuttingInsertEntity.all().map {
                it.toCuttingInsert()
            }
        }
    }
}