package com.mindovercnc.linuxcnc

import com.mindovercnc.base.ToolsRepository
import com.mindovercnc.base.data.LatheTool
import com.mindovercnc.base.data.tools.*
import com.mindovercnc.database.entity.CuttingInsertEntity
import com.mindovercnc.database.entity.LatheCutterEntity
import com.mindovercnc.database.entity.ToolHolderEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class ToolsRepositoryImpl(
    private val scope: CoroutineScope,
    private val toolTableFilePath: String
) : ToolsRepository {

    private val toolList = MutableStateFlow(emptyList<LatheTool>())
    private val toolMap = mutableMapOf<Int, String>()

    init {
        //when a tool offset is updated, the file is rewritten, so we reload it
        FileWatcher.watchChanges(scope, toolTableFilePath, true) {
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
                    latheCutter = it.cutter?.toLatheCutter(),
                    xOffset = it.xOffset,
                    zOffset = it.zOffset
                )
            }
        }
    }

    private fun LatheCutterEntity.toLatheCutter(): LatheCutter? {
        return when (type) {
            LatheCutter.Turning::class.simpleName -> LatheCutter.Turning(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                tipOrientation = TipOrientation.getOrientation(tipOrientation),
                allowedSpindleDirection = AllowedSpindleDirection.valueOf(spindleDirection)
            )
            LatheCutter.Boring::class.simpleName -> LatheCutter.Boring(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                tipOrientation = TipOrientation.getOrientation(tipOrientation),
                allowedSpindleDirection = AllowedSpindleDirection.valueOf(spindleDirection),
                minBoreDiameter = minBoreDiameter ?: 0.0,
                maxZDepth = maxZDepth ?: 0.0
            )
            LatheCutter.DrillingReaming::class.simpleName -> LatheCutter.DrillingReaming(
                cutterId = id.value,
                insert = null,
                toolDiameter = toolDiameter!!,
                maxZDepth = maxZDepth ?: 0.0
            )
            LatheCutter.Parting::class.simpleName -> LatheCutter.Parting(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                bladeWidth = bladeWidth!!,
                maxXDepth = maxXDepth ?: 0.0
            )
            LatheCutter.Grooving::class.simpleName -> LatheCutter.Grooving(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                tipOrientation = TipOrientation.getOrientation(tipOrientation),
                allowedSpindleDirection = AllowedSpindleDirection.valueOf(spindleDirection),
                bladeWidth = bladeWidth!!,
                maxXDepth = maxXDepth ?: 0.0
            )
            LatheCutter.OdThreading::class.simpleName -> LatheCutter.OdThreading(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                minPitch = minThreadPitch ?: 0.0,
                maxPitch = maxThreadPitch ?: 0.0
            )
            LatheCutter.IdThreading::class.simpleName -> LatheCutter.IdThreading(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                minPitch = minThreadPitch ?: 0.0,
                maxPitch = maxThreadPitch ?: 0.0
            )
            LatheCutter.Slotting::class.simpleName -> LatheCutter.Slotting(
                cutterId = id.value,
                insert = insert!!.toCuttingInsert(),
                bladeWidth = bladeWidth!!,
                maxZDepth = maxZDepth ?: 0.0
            )
            else -> null
        }
    }

    private fun CuttingInsertEntity.toCuttingInsert(): CuttingInsert {
        return CuttingInsert(
            madeOf = CuttingInsert.MadeOf.valueOf(madeOf),
            code = code,
            tipRadius = radius,
            frontAngle = frontAngle,
            backAngle = backAngle
        )
    }

    override fun addOrUpdateToolHolder(toolHolder: ToolHolder) {
        ToolHolderEntity.new {
            holderNumber = toolHolder.holderNumber
            holderType = toolHolder.type
            cutter = null
            clampingPosition = 0
            xOffset = toolHolder.xOffset
            zOffset = toolHolder.zOffset
        }
    }

    override fun removeToolHolder(toolHolder: ToolHolder) {

    }

    override fun getLatheCutters(): List<LatheCutter> {
        return LatheCutterEntity.all().mapNotNull { it.toLatheCutter() }
    }

    override fun addOrUpdateLatheCutter(latheCutter: LatheCutter) {
        TODO("Not yet implemented")
    }

    override fun removeLatheCutter(latheCutter: LatheCutter) {
        TODO("Not yet implemented")
    }

    private fun readFile() {
        val newToolList = mutableListOf<LatheTool>()
        toolMap.clear()
        File(toolTableFilePath).forEachLine { aLine ->
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

    private fun parseToolLine(line: String): LatheTool {
        val builder = LatheTool.Builder()
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
            File(toolTableFilePath).printWriter().use {
                toolMap.entries.forEach { line ->
                    it.println(line.value)
                }
                it.close()
            }
        }
    }
}