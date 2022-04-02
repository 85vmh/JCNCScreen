package screen.composables.tabstatus

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import extensions.draggableScroll
import org.kodein.di.compose.rememberInstance
import screen.composables.VerticalDivider
import usecase.MessagesUseCase
import usecase.model.Message

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MessagesView(
    modifier: Modifier
) {
    val scope = rememberCoroutineScope()
    val messagesUseCase by rememberInstance<MessagesUseCase>()
    val messageList by messagesUseCase.getAllMessages().collectAsState(emptyList())

    val scrollState = rememberLazyListState()

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            stickyHeader {
                MessagesHeader()
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
            items(messageList) { item ->
                MessageRow(item)
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun MessagesHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center,
            text = "Type"
        )
        VerticalDivider()
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = "Message"
        )
        VerticalDivider()
    }
}

@Composable
fun MessageRow(item: Message, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(60.dp)
            .clickable {

            }
    ) {
        Text(
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center,
            text = item.level.name
        )
        VerticalDivider()
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(item.text)
        }
        VerticalDivider()
    }
}
