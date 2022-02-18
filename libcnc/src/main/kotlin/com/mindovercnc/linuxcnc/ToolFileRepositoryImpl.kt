package com.mindovercnc.linuxcnc

import com.mindovercnc.base.ToolFileRepository
import com.mindovercnc.base.data.LatheTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class ToolFileRepositoryImpl(
    private val scope: CoroutineScope,
    private val toolTableFilePath: String
) : ToolFileRepository {

    private val toolList = MutableStateFlow(emptyList<LatheTool>())
    private val toolMap = mutableMapOf<Int, String>()

    init {
        FileWatcher.watchChanges(scope, toolTableFilePath, true) {
            println("---$toolTableFilePath changed, reloading")
            readFile()
        }
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
                    LatheTool.Orientation.getOrientation(orientValue)?.let { toolOrientation ->
                        builder.orientation = toolOrientation
                    }
                }
                element.startsWith(";") -> break
            }
        }
        if (line.contains(";")) {
            builder.comment = line.substring(line.indexOf(";"))
        }
        return builder.build()
    }

    override fun getTools(): Flow<List<LatheTool>> {
        return toolList
    }

    override fun addOrUpdateTool(latheTool: LatheTool) {
        toolMap[latheTool.toolNo] = latheTool.toFormattedString()
        updateToolTableFile()
    }

    override fun removeTool(toolNo: Int) {
        toolMap.remove(toolNo)
        updateToolTableFile()
    }

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