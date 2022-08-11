package com.mindovercnc.linuxcnc

import com.mindovercnc.database.entity.CuttingInsertEntity
import com.mindovercnc.database.table.CuttingInsertTable
import com.mindovercnc.model.CuttingInsert
import com.mindovercnc.repository.CuttingInsertsRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CuttingInsertsRepositoryImpl : CuttingInsertsRepository {

    override fun insert(cuttingInsert: CuttingInsert) {
        transaction {
            CuttingInsertEntity.new {
                madeOf = cuttingInsert.madeOf
                code = cuttingInsert.code
                tipRadius = cuttingInsert.tipRadius
                tipAngle = cuttingInsert.tipAngle
                size = cuttingInsert.size
            }
        }
    }

    override fun update(cuttingInsert: CuttingInsert) {
        transaction {
            CuttingInsertTable.update({ CuttingInsertTable.id eq cuttingInsert.id }) {
                it[code] = cuttingInsert.code
                it[madeOf] = cuttingInsert.madeOf
                it[tipRadius] = cuttingInsert.tipRadius
                it[tipAngle] = cuttingInsert.tipAngle
                it[size] = cuttingInsert.size
            }
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