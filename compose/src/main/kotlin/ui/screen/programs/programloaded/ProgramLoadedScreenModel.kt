package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import com.mindovercnc.base.FileSystemRepository
import com.mindovercnc.base.GCodeRepository
import kotlinx.coroutines.flow.update
import screen.composables.editor.Editor
import usecase.model.FileSystemItem
import java.io.File

class ProgramLoadedScreenModel(
    private val gCodeRepository: GCodeRepository
) : StateScreenModel<ProgramLoadedScreenModel.State>(State()) {

    data class State(
        val currentFolder: FileSystemItem? = null,
        val editor: Editor? = null
    )

    init {
        gCodeRepository.parseFile(File("/home/vasimihalca/Work/linuxcnc-dev/nc_files/conversational/Test_od_turning.ngc"))
    }
}