package usecase

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.TaskState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import usecase.model.Message

class MessagesUseCase(
    private val cncStatusRepository: CncStatusRepository
) {
    fun getMessage(): Flow<Message> {
        return getMessages()
    }

    private fun getMessages(): Flow<Message> {
        return cncStatusRepository.cncStatusFlow()
            .map {
                when (it.taskStatus.taskState) {
                    TaskState.EStop -> Message(
                        text = "Estop is pressed!",
                        level = Message.Level.ERROR,
                        persistence = Message.Persistence.PERSISTENT
                    )
                    TaskState.MachineOff,
                    TaskState.EStopReset -> Message(
                        text = "Machine not powered ON",
                        level = Message.Level.WARNING,
                        persistence = Message.Persistence.PERSISTENT
                    )
                    TaskState.MachineOn -> Message(
                        text = "Machine is ON",
                        level = Message.Level.INFO,
                        persistence = Message.Persistence.TRANSIENT
                    )
                }
            }
    }
}