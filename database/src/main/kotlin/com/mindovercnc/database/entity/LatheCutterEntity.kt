package com.mindovercnc.database.entity

import com.mindovercnc.database.table.LatheCutterTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LatheCutterEntity(id: EntityID<Int>) : IntEntity(id) {
    var insert by CuttingInsertEntity optionalReferencedOn LatheCutterTable.insertId
    var type by LatheCutterTable.type
    var tipOrientation by LatheCutterTable.tipOrientation
    var spindleDirection by LatheCutterTable.spindleDirection
    var toolDiameter by LatheCutterTable.toolDiameter
    var minBoreDiameter by LatheCutterTable.minBoreDiameter
    var maxZDepth by LatheCutterTable.maxZDepth
    var bladeWidth by LatheCutterTable.bladeWidth
    var maxXDepth by LatheCutterTable.maxXDepth
    var minThreadPitch by LatheCutterTable.minThreadPitch
    var maxThreadPitch by LatheCutterTable.maxThreadPitch

    companion object : IntEntityClass<LatheCutterEntity>(LatheCutterTable)
}