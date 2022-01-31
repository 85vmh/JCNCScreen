package com.mindovercnc.base.data

import kotlinx.coroutines.CoroutineScope

interface AppFile {
    val name: String
    val path: String
    val isDirectory: Boolean
    val children: List<AppFile>
    val hasChildren: Boolean
    val size: Long
    val lastModified: Long

    fun readLines(scope: CoroutineScope): TextLines
}