package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.AppFile
import com.mindovercnc.linuxcnc.toAppFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import screen.composables.editor.Editor
import usecase.model.FileSystemItem
import java.io.File
import java.time.Instant
import java.util.*

class ProgramsUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val settingsRepository: SettingsRepository,
    fileSystemRepository: FileSystemRepository
) {

    private val selectedFolder = MutableStateFlow(fileSystemRepository.getNcRootAppFile())
    private val selectedFile = MutableStateFlow<AppFile?>(null)

    val currentFileSystemItem = selectedFolder.asStateFlow().map { it.toFileSystemItem() }

    val currentEditor = selectedFile.asStateFlow().map {
        if (it != null) Editor(it) else null
    }

    val currentFile = selectedFile.asStateFlow()

    fun loadFolderContents(path: String) {
        selectedFolder.value = File(path).toAppFile()
        selectedFile.value = null
    }

    private fun AppFile.toFileSystemItem(loadChildren: Boolean = true): FileSystemItem {
        return FileSystemItem(
            name = this.name,
            path = this.path,
            isDirectory = this.isDirectory,
            children = when {
                loadChildren -> this.children
                    .map { it.toFileSystemItem(false) }
                    .sortedWith(compareBy({ it.isDirectory }, { it.name }))
                else -> emptyList()
            },
            lastModified = Date.from(Instant.ofEpochMilli(this.lastModified)),
        ) {
            if (isDirectory) {
                println("---Folder clicked: ${this.path}")
                selectedFolder.value = File(this.path).toAppFile()
                selectedFile.value = null
            } else {
                selectedFile.value = File(this.path).toAppFile()
            }
        }
    }

    fun loadSelectedProgram(): Boolean {
        currentFile.value?.let {
            println("Loading program file: ${it.path}")
            commandRepository.loadProgramFile(it.path)
        }
        return true
    }
}
