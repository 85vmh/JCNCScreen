package com.mindovercnc.base

import com.mindovercnc.base.model.GcodeCommand
import java.io.File

interface GCodeRepository {
    fun parseFile(file: File): List<GcodeCommand>
}