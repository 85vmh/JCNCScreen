package screen.composables.platform

import kotlinx.coroutines.CoroutineScope
import screen.composables.extension.toProjectFile
import screen.composables.util.TextLines

val NcFilesFolder: File get() = java.io.File("/home/vasimihalca/Work/linuxcnc-dev/nc_files").toProjectFile()

interface File {
    val name: String
    val isDirectory: Boolean
    val children: List<File>
    val hasChildren: Boolean

    fun readLines(scope: CoroutineScope): TextLines
}