package screen.composables

import androidx.compose.foundation.background
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
fun MessageView(message: Flow<Message>) {
    val str by message.collectAsState(null)
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(40.dp)
            .background(Color.Gray)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically).padding(start = 16.dp),
            text = "Message is: ${str?.text}"
        )
    }
}