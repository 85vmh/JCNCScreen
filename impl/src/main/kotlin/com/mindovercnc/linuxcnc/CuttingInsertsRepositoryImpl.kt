package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CuttingInsertsRepository
import com.mindovercnc.base.data.tools.CuttingInsert
import com.mindovercnc.database.entity.CuttingInsertEntity
import org.jetbrains.exposed.sql.transactions.transaction

class CuttingInsertsRepositoryImpl : CuttingInsertsRepository {

    override fun insert(cuttingInsert: CuttingInsert) {
        CuttingInsertEntity.new {
            madeOf = cuttingInsert.madeOf
            code = cuttingInsert.code
            radius = cuttingInsert.tipRadius
            frontAngle = cuttingInsert.frontAngle
            backAngle = cuttingInsert.backAngle
        }
    }

    override fun findAll(): List<CuttingInsert> {
        return transaction {
            CuttingInsertEntity.all().map {
                CuttingInsert(
                    madeOf = it.madeOf,
                    code = it.code,
                    tipRadius = it.radius,
                    frontAngle = it.frontAngle,
                    backAngle = it.backAngle
                )
            }
        }
    }
}