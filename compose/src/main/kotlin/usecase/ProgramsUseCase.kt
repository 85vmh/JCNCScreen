package usecase

import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.FileSystemRepository
import com.mindovercnc.base.SettingsRepository
import com.mindovercnc.base.data.Position
import com.mindovercnc.base.data.dtg
import com.mindovercnc.base.data.getDisplayablePosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import screen.composables.editor.Editor
import screen.uimodel.AxisPosition
import screen.uimodel.PositionUiModel
import usecase.model.FileSystemItem
import java.io.File

class ProgramsUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val settingsRepository: SettingsRepository,
    fileSystemRepository: FileSystemRepository
) {

    private fun getG5xPosition(): Flow<Position> {
        return statusRepository.cncStatusFlow()
            .map { it.getDisplayablePosition() }
            .distinctUntilChanged()
    }

    private fun getDtgPosition(): Flow<Position> {
        return statusRepository.cncStatusFlow()
            .map { it.dtg }
            .distinctUntilChanged()
    }

    val uiModel = combine(
        getG5xPosition(),
        getDtgPosition(),
    ) { g5xPos, dtgPos ->
        val xAxisPos = AxisPosition(AxisPosition.Axis.X, g5xPos.x, dtgPos.x, units = AxisPosition.Units.MM)
        val zAxisPos = AxisPosition(AxisPosition.Axis.Z, g5xPos.z, dtgPos.z, units = AxisPosition.Units.MM)
        PositionUiModel(xAxisPos, zAxisPos, true)
    }

    private val selectedFolder = MutableStateFlow(fileSystemRepository.getNcRootAppFile())
    private val selectedFile = MutableStateFlow<File?>(null)

    val currentFolder = selectedFolder.asStateFlow().map { it.toFileSystemItem() }

    val currentEditor = selectedFile.asStateFlow().map {
        if (it != null) Editor(it) else null
    }

    val currentFile = selectedFile.asStateFlow()

    fun loadFolderContents(path: String) {
        selectedFolder.value = File(path)
        selectedFile.value = null
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
                selectedFolder.value = File(this.path)
                selectedFile.value = null
            }
        } else {
            FileSystemItem.FileItem(
                name = this.name,
                path = this.path,
                extension = this.extension,
                lastModified = this.lastModified(),
                size = this.length()
            ) {
                selectedFile.value = File(this.path)
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

    fun loadSelectedProgram(): Boolean {
        currentFile.value?.let {
            println("Loading program file: ${it.path}")
            commandRepository.loadProgramFile(it.path)
        }
        return true
    }

    fun runProgram(){
        commandRepository.runProgram()
    }

    fun stopProgram(){
        commandRepository.stopProgram()
    }
}
