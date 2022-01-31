package usecase

import com.mindovercnc.base.MessagesRepository
import com.mindovercnc.base.data.SystemMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import usecase.model.Message

class MessagesUseCase(
    private val messagesRepository: MessagesRepository
) {
    fun getAllMessages(): Flow<List<Message>> {
        return messagesRepository.messagesFlow()
            .map {
                val result = mutableListOf<Message>()
                it.emcMessages.forEach { emcMsg ->
                    when (emcMsg.type) {
                        SystemMessage.MessageType.NMLError,
                        SystemMessage.MessageType.OperatorError -> result.add(Message(emcMsg.message, Message.Level.ERROR))
                        else -> result.add(Message(emcMsg.message, Message.Level.INFO))
                    }
                }
                it.uiMessages.forEach { uiMsg ->
                    val level = if (uiMsg.key.isError) Message.Level.ERROR else Message.Level.WARNING
                    result.add(Message(uiMsg.key.name, level))
                }
                result
            }
            .onEach {
                println("Message list is: $it")
            }
    }
}