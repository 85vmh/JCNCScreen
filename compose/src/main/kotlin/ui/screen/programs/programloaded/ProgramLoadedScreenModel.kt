package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import com.mindovercnc.base.FileSystemRepository
import kotlinx.coroutines.flow.update
import screen.composables.editor.Editor
import usecase.model.FileSystemItem
import java.io.File

class ProgramLoadedScreenModel(
) : StateScreenModel<ProgramLoadedScreenModel.State>(State()) {

    data class State(
        val currentFolder: FileSystemItem? = null,
        val editor: Editor? = null
    )
}