package ui.screen.programs.root

import cafe.adriel.voyager.core.model.StateScreenModel
import com.mindovercnc.base.FileSystemRepository
import kotlinx.coroutines.flow.update
import screen.composables.editor.Editor
import usecase.model.FileSystemItem
import java.io.File

class ProgramsRootScreenModel(
    fileSystemRepository: FileSystemRepository
) : StateScreenModel<ProgramsRootScreenModel.State>(State()) {

    data class State(
        val currentFolder: FileSystemItem? = null,
        val editor: Editor? = null
    )

    init {
        setCurrentFolder(fileSystemRepository.getNcRootAppFile())
    }

    private fun setCurrentFolder(file: File) {
        mutableState.update {
            it.copy(
                currentFolder = file.toFileSystemItem(),
            )
        }
    }

    private fun setCurrentFile(file: File?) {
        mutableState.update {
            it.copy(
                editor = if (file != null) Editor(file) else null,
            )
        }
    }

    fun loadFolderContents(path: String) {
        setCurrentFolder(File(path))
        setCurrentFile(null)
    }

    private fun File.toFileSystemItem(loadChildren: Boolean = true): FileSystemItem {
        return if (this.isDirectory) {
            FileSystemItem.FolderItem(
                name = this.name,
                path = this.path,
                lastModified = this.lastModified(),
                size = this.length(),
                children = when {
                    loadChildren -> this.listFiles().orEmpty()
                        .filter { it.isDisplayable() }
                        .map { it.toFileSystemItem(false) }
                        .sortedWith(compareBy({ it is FileSystemItem.FolderItem }, { it.lastModified }))
                    else -> emptyList()
                }
            ) {
                println("---Folder clicked: ${this.path}")
                setCurrentFolder(File(this.path))
                setCurrentFile(null)
            }
        } else {
            FileSystemItem.FileItem(
                name = this.name,
                path = this.path,
                extension = this.extension,
                lastModified = this.lastModified(),
                size = this.length()
            ) {
                setCurrentFile(File(this.path))
            }
        }
    }

    private fun File.isDisplayable(): Boolean {
        return if (isDirectory) {
            !isHidden
        } else {
            !isHidden && extension.equals("ngc", true)
        }
    }
}