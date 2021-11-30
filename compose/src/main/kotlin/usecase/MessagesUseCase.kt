package usecase

import com.mindovercnc.base.MessagesRepository
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
                    result.add(Message(emcMsg.message, Message.Level.ERROR))
                }
                it.uiMessages.forEach { uiMsg ->
                    result.add(Message(uiMsg.key.name, Message.Level.WARNING))
                }
                result
            }
            .onEach {
                println("Message list is: $it")
            }
    }
}