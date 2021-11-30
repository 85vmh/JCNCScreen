package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import usecase.model.Message

@Composable
fun HeaderView(message: Flow<List<Message>>, onClick: () -> Unit) {
    val messageList by message.collectAsState(emptyList())

    val lastMessage = messageList.lastOrNull()
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick.invoke() }
            .height(40.dp)
            .background(Color.Gray)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically).padding(start = 16.dp),
            text = "Message is: ${lastMessage?.text}"
        )
    }
}