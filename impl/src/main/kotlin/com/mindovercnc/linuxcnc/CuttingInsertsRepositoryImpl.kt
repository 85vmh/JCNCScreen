package com.mindovercnc.linuxcnc

import com.mindovercnc.base.CuttingInsertsRepository
import com.mindovercnc.base.data.tools.CuttingInsert
import com.mindovercnc.database.entity.CuttingInsertEntity

class CuttingInsertsRepositoryImpl : CuttingInsertsRepository {

    override fun insert(cuttingInsert: CuttingInsert) {
        CuttingInsertEntity.new {
            madeOf = cuttingInsert.madeOf.name
            code = cuttingInsert.code
            radius = cuttingInsert.tipRadius
            frontAngle = cuttingInsert.frontAngle
            backAngle = cuttingInsert.backAngle
        }
    }

    override fun findAll(): List<CuttingInsert> {
        return CuttingInsertEntity.all().map {
            CuttingInsert(
                madeOf = CuttingInsert.MadeOf.valueOf(it.madeOf),
                code = it.code,
                tipRadius = it.radius,
                frontAngle = it.frontAngle,
                backAngle = it.backAngle
            )
        }
    }
}