package com.mindovercnc.base

import java.io.File

interface GCodeRepository {
    fun parseFile(file: File)
}