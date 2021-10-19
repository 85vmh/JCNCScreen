package screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.TaskState
import com.mindovercnc.base.data.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RootScreenViewModel constructor(
    private val cncStatusRepository: CncStatusRepository,
    private val cncCommandRepository: CncCommandRepository,
    startupScreen: RootScreen
) {
    var screen by mutableStateOf(startupScreen)

    fun getMessages(): Flow<String> {
        return cncStatusRepository.cncStatusFlow()
            .map {
                when (it.taskStatus.taskState) {
                    TaskState.EStop -> "Estop is pressed!"
                    TaskState.MachineOff,
                    TaskState.EStopReset -> "Machine not powered ON"
                    else -> ""
                }
            }
    }

}