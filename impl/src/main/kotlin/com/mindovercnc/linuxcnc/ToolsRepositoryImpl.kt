package com.mindovercnc.linuxcnc

import com.mindovercnc.repository.ToolsRepository
import com.mindovercnc.database.entity.CuttingInsertEntity
import com.mindovercnc.database.entity.LatheToolEntity
import com.mindovercnc.database.entity.ToolHolderEntity
import com.mindovercnc.model.CuttingInsert
import com.mindovercnc.model.LatheTool
import com.mindovercnc.model.TipOrientation
import com.mindovercnc.model.ToolHolder
import com.mindovercnc.model.ToolType
import com.mindovercnc.database.table.CuttingInsertTable
import com.mindovercnc.database.table.LatheToolTable
import com.mindovercnc.database.table.ToolHolderTable
import com.mindovercnc.database.table.ToolHolderTable.holderNumber
import com.mindovercnc.model.LinuxCncTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


fun CuttingInsertEntity.toCuttingInsert(): CuttingInsert {
    return CuttingInsert(
        madeOf = madeOf,
        code = code,
        tipRadius = tipRadius,
        tipAngle = tipAngle,
    )
}
class ToolsRepositoryImpl(
    private val scope: CoroutineScope,
    private val toolTableFilePath: ToolFilePath
) : ToolsRepository {

    private val toolList = MutableStateFlow(emptyList<LinuxCncTool>())
    private val toolMap = mutableMapOf<Int, String>()

    init {
        //when a tool offset is updated, the file is rewritten, so we reload it
        FileWatcher.watchChanges(scope, toolTableFilePath.file.path, true) {
            println("---$toolTableFilePath changed, reloading")
            readFile()
        }
    }

    override fun getToolHolders(): List<ToolHolder> {
        return transaction {
            ToolHolderEntity.all().map {
                ToolHolder(
                    holderNumber = it.holderNumber,
                    type = it.holderType,
                    latheTool = it.cutter?.toLatheTool(),
                    xOffset = it.xOffset,
                    zOffset = it.zOffset
                )
            }
        }
    }

    private fun LatheToolEntity.toLatheTool(): LatheTool? {
        return when (type) {
            ToolType.Turning -> LatheTool.Turning(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                tipOrientation = TipOrientation.getOrientation(tipOrientation),
                frontAngle = frontAngle!!,
                backAngle = backAngle!!,
                spindleDirection = spindleDirection,
            )
            ToolType.Boring -> LatheTool.Boring(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                tipOrientation = TipOrientation.getOrientation(tipOrientation),
                frontAngle = frontAngle!!,
                backAngle = backAngle!!,
                spindleDirection = spindleDirection,
                minBoreDiameter = minBoreDiameter ?: 0.0,
                maxZDepth = maxZDepth ?: 0.0
            )
            ToolType.DrillingReaming -> LatheTool.DrillingReaming(
                toolId = id.value,
                insert = null,
                toolDiameter = toolDiameter!!,
                maxZDepth = maxZDepth ?: 0.0
            )
            ToolType.Parting -> LatheTool.Parting(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                bladeWidth = bladeWidth!!,
                maxXDepth = maxXDepth ?: 0.0
            )
            ToolType.Grooving -> LatheTool.Grooving(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                tipOrientation = TipOrientation.getOrientation(tipOrientation),
                spindleDirection = spindleDirection,
                bladeWidth = bladeWidth!!,
                maxXDepth = maxXDepth ?: 0.0
            )
            ToolType.OdThreading -> LatheTool.OdThreading(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                minPitch = minThreadPitch ?: 0.0,
                maxPitch = maxThreadPitch ?: 0.0
            )
            ToolType.IdThreading -> LatheTool.IdThreading(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                minPitch = minThreadPitch ?: 0.0,
                maxPitch = maxThreadPitch ?: 0.0
            )
            ToolType.Slotting -> LatheTool.Slotting(
                toolId = id.value,
                insert = insert!!.toCuttingInsert(),
                bladeWidth = bladeWidth!!,
                maxZDepth = maxZDepth ?: 0.0
            )
            else -> null
        }
    }

    override fun createToolHolder(toolHolder: ToolHolder) {
        transaction {
            ToolHolderEntity.new {
                holderNumber = toolHolder.holderNumber
                holderType = toolHolder.type
                cutter = null
                clampingPosition = 0
                xOffset = toolHolder.xOffset
                zOffset = toolHolder.zOffset
            }
        }
    }

    override fun updateToolHolder(toolHolder: ToolHolder) {
        transaction {
            ToolHolderTable.update({ holderNumber eq toolHolder.holderNumber }) {
                it[holderType] = toolHolder.type
                it[cutterId] = toolHolder.latheTool?.toolId
                it[clampingPosition] = toolHolder.clampingPosition
                //offsets in a separate call
            }
        }
    }

    override fun deleteToolHolder(toolHolder: ToolHolder): Boolean {
        return transaction {
            ToolHolderTable.deleteWhere { ToolHolderTable.holderNumber eq toolHolder.holderNumber } != 0
        }
    }

    override fun getLatheTools(): List<LatheTool> {
        return transaction {
            LatheToolEntity.all().mapNotNull { it.toLatheTool() }
        }
    }

    override fun createLatheCutter(latheTool: LatheTool) {
        when (latheTool) {
            is LatheTool.Turning -> LatheToolEntity.new {
                insert = getInsertById(latheTool.insert.id!!)
                type = ToolType.Turning
                tipOrientation = latheTool.tipOrientation.orient
                spindleDirection = latheTool.spindleDirection
            }
            is LatheTool.Boring -> LatheToolEntity.new {
                insert = getInsertById(latheTool.insert.id!!)
                type = ToolType.Boring
                tipOrientation = latheTool.tipOrientation.orient
                spindleDirection = latheTool.spindleDirection
                minBoreDiameter = latheTool.minBoreDiameter
                maxZDepth = latheTool.maxZDepth
            }
        }
    }

    private fun getInsertById(insertId: Int): CuttingInsertEntity {
        return transaction {
            CuttingInsertEntity.find { CuttingInsertTable.id eq insertId }.first()
        }
    }

    override fun updateLatheCutter(latheTool: LatheTool) {
        TODO("Not yet implemented")
    }

    override fun deleteLatheTool(latheTool: LatheTool): Boolean {
        return transaction {
            LatheToolTable.deleteWhere { LatheToolTable.id eq latheTool.toolId } != 0
        }
    }

    private fun readFile() {
        val newToolList = mutableListOf<LinuxCncTool>()
        toolMap.clear()
        toolTableFilePath.file.forEachLine { aLine ->
            with(parseToolLine(aLine)) {
                newToolList.add(this)
                toolMap[this.toolNo] = aLine
            }
        }
        toolList.value = emptyList()
        toolList.update {
            it.plus(newToolList)
        }
    }

    private fun parseToolLine(line: String): LinuxCncTool {
        val builder = LinuxCncTool.Builder()
        val elements = line.split(" ")
        for (element in elements) {
            when {
                element.startsWith("T") -> builder.toolNo = element.substring(1).toInt()
                element.startsWith("X") -> builder.xOffset = element.substring(1).toDouble()
                element.startsWith("Z") -> builder.zOffset = element.substring(1).toDouble()
                element.startsWith("D") -> builder.tipRadius = element.substring(1).toDouble()
                element.startsWith("I") -> builder.frontAngle = element.substring(1).toDouble()
                element.startsWith("J") -> builder.backAngle = element.substring(1).toDouble()
                element.startsWith("Q") -> {
                    val orientValue = element.substring(1).toInt()
                    builder.orientation = TipOrientation.getOrientation(orientValue)
                }
                element.startsWith(";") -> break
            }
        }
        if (line.contains(";")) {
            builder.comment = line.substring(line.indexOf(";"))
        }
        return builder.build()
    }

//    override fun getTools(): Flow<List<LatheTool>> {
//        return toolList
//    }
//
//    override fun addOrUpdateTool(latheTool: LatheTool) {
//        toolMap[latheTool.toolNo] = latheTool.toFormattedString()
//        updateToolTableFile()
//    }
//
//    override fun removeTool(toolNo: Int) {
//        toolMap.remove(toolNo)
//        updateToolTableFile()
//    }

    private fun updateToolTableFile() {
        scope.launch(Dispatchers.IO) {
            toolTableFilePath.file.printWriter().use {
                toolMap.entries.forEach { line ->
                    it.println(line.value)
                }
                it.close()
            }
        }
    }
}