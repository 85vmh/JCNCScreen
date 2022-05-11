package com.mindovercnc.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object CuttingInsertTable : IntIdTable() {
    val madeOf = varchar("made_of", length = 50)
    val code = varchar("code", 50)
    val radius = double("radius")
    val frontAngle = double("front_angle")
    val backAngle = double("back_angle")
}