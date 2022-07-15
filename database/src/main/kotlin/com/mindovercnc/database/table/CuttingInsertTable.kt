package com.mindovercnc.database.table

import com.mindovercnc.model.MadeOf
import org.jetbrains.exposed.dao.id.IntIdTable

object CuttingInsertTable : IntIdTable() {
    val madeOf = enumeration("made_of", MadeOf::class)
    val code = varchar("code", 50)
    val tipRadius = double("tip_radius")
    val tipAngle = double("tip_angle")
    val size = double("size")
}