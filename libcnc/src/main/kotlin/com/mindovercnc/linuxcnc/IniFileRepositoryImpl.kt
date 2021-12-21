package com.mindovercnc.linuxcnc

import com.mindovercnc.base.IniFileRepository
import com.mindovercnc.base.data.IniFile
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

class IniFileRepositoryImpl(
    iniFilePath: String
) : IniFileRepository {
    private var parsedFile: Map<String, Map<String, String>>
    val rootPath = iniFilePath.substring(0, iniFilePath.lastIndexOf("/") + 1)

    init {
        parsedFile = parseIniFile(iniFilePath)
    }

    override fun getIniFile(): IniFile {
        var programPrefix = ""
        var parameterFile = rootPath
        var toolTableFile = rootPath
        val jointParameters = mutableListOf<IniFile.JointParameters>()

        if (parsedFile.keys.contains(Section.DISPLAY.name)) {
            parsedFile[Section.DISPLAY.name]?.let { displaySection ->
                if (displaySection.keys.contains(Parameter.PROGRAM_PREFIX.name)) {
                    displaySection[Parameter.PROGRAM_PREFIX.name]?.let {
                        programPrefix = it
                    }
                }
            }
        }
        if (parsedFile.keys.contains(Section.RS274NGC.name)) {
            parsedFile[Section.RS274NGC.name]?.let { displaySection ->
                if (displaySection.keys.contains(Parameter.PARAMETER_FILE.name)) {
                    displaySection[Parameter.PARAMETER_FILE.name]?.let {
                        parameterFile += it
                    }
                }
            }
        }
        if (parsedFile.keys.contains(Section.EMCIO.name)) {
            parsedFile[Section.EMCIO.name]?.let { displaySection ->
                if (displaySection.keys.contains(Parameter.TOOL_TABLE.name)) {
                    displaySection[Parameter.TOOL_TABLE.name]?.let {
                        toolTableFile += it
                    }
                }
            }
        }
        getJointParameters(Section.JOINT_0)?.let {
            jointParameters.add(it)
        }
        getJointParameters(Section.JOINT_1)?.let {
            jointParameters.add(it)
        }

        return IniFile(
            subroutinePath = "",
            programPrefix = programPrefix,
            parameterFile = parameterFile,
            toolTableFile = toolTableFile,
            joints = jointParameters
        )
    }

    private fun getJointParameters(jointIndex: Section): IniFile.JointParameters? {
        if (parsedFile.keys.contains(jointIndex.name)) {
            parsedFile[jointIndex.name]?.let { displaySection ->
                var minLimit: Double? = null
                if (displaySection.keys.contains(Parameter.MIN_LIMIT.name)) {
                    displaySection[Parameter.MIN_LIMIT.name]?.let {
                        minLimit = it.toDouble()
                    }
                }
                var maxLimit: Double? = null
                if (displaySection.keys.contains(Parameter.MAX_LIMIT.name)) {
                    displaySection[Parameter.MAX_LIMIT.name]?.let {
                        maxLimit = it.toDouble()
                    }
                }
                var home: Double? = null
                if (displaySection.keys.contains(Parameter.HOME.name)) {
                    displaySection[Parameter.HOME.name]?.let {
                        home = it.toDouble()
                    }
                }
                var homeOffset: Double? = null
                if (displaySection.keys.contains(Parameter.HOME_OFFSET.name)) {
                    displaySection[Parameter.HOME_OFFSET.name]?.let {
                        homeOffset = it.toDouble()
                    }
                }
                if (minLimit != null && maxLimit != null) {
                    return IniFile.JointParameters(minLimit!!, maxLimit!!, home ?: 0.0, homeOffset ?: 0.0)
                }
            }
        }
        return null
    }


    enum class Section {
        EMC, DISPLAY, FILTER, TASK, RS274NGC, EMCMOT, EMCIO, HAL, TRAJ, KINS, AXIS_X, AXIS_Y, AXIS_Z, JOINT_0, JOINT_1, JOINT_2, ;
    }

    enum class ParamType {
        STRING, DOUBLE
    }

    enum class Parameter(paramType: ParamType) {
        PROGRAM_PREFIX(ParamType.STRING),
        PARAMETER_FILE(ParamType.STRING),
        TOOL_TABLE(ParamType.STRING),
        MIN_LIMIT(ParamType.DOUBLE),
        MAX_LIMIT(ParamType.DOUBLE),
        HOME(ParamType.DOUBLE),
        HOME_OFFSET(ParamType.DOUBLE)
    }


    private fun parseIniFile(fileName: String): Map<String, Map<String, String>> {
        val properties: MutableMap<String, Map<String, String>> = HashMap()
        var aLn: String?
        try {
            val buffReader = BufferedReader(FileReader(fileName))
            while (buffReader.readLine().also { aLn = it } != null) {

                var line = aLn?.replace("\t", "")?.trim() ?: ""

                if (line.isEmpty()) continue
                if (line.startsWith("#")) continue
                if (line.startsWith("[")) {
                    do {
                        val group = line.substring(1, line.length - 1)
                        val subMap: MutableMap<String, String> = HashMap()
                        var parts: Array<String>?
                        properties[group] = subMap
                        while (buffReader.readLine().also { it?.let { line = it } } != null) {
                            if (line.isEmpty()) continue
                            line = line.replace("\t", "").trim()
                            if (line.isEmpty()) continue
                            if (line.startsWith("#")) continue
                            if (line.startsWith("[")) break
                            parts = line.split("\\s*=\\s*".toRegex(), 2).toTypedArray()

                            if (subMap.containsKey(parts[0])) {
                                System.err.println("may be duplicate entry? [" + parts[0] + "]")
                                val sb = StringBuilder(subMap[parts[0]])
                                sb.append(",")
                                sb.append(parts[1])
                                subMap[parts[0]] = sb.toString()
                            } else subMap[parts[0]] = parts[1]
                        }
                    } while (line.startsWith("["))
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return properties
    }
}