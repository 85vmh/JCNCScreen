package com.mindovercnc.database.table

import com.mindovercnc.base.data.tools.MadeOf
import org.jetbrains.exposed.dao.id.IntIdTable

object CuttingInsertTable : IntIdTable() {
    val madeOf = enumeration("made_of", MadeOf::class)
    val code = varchar("code", 50)
    val radius = double("radius")
    val frontAngle = double("front_angle")
    val backAngle = double("back_angle")
}