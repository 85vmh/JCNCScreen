package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.StringBuilder

@Composable
@Preview
private fun NumPadViewPreview() {
    NumPadView(modifier = Modifier.fillMaxSize())
}

@Composable
fun NumPadView(modifier: Modifier) {
    var string by remember { mutableStateOf("") }
    Column(modifier = modifier) {
        NumPadRow("1", "2", "3") { string += it }
        NumPadRow("4", "5", "6") { string += it }
        NumPadRow("7", "8", "9") { string += it }
        NumPadRow("+/-", "0", ".") { string += it }

        Text(string)
    }
}

@Composable
fun NumPadKey(key: String, onClick: (String) -> Unit) {
    val shape = CircleShape
    Box(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 50.dp)
                .background(Color.Gray, shape = shape)
                .clip(shape)
                .clickable { onClick(key) },
            contentAlignment = Alignment.Center
        ) {
            Text(key)
        }
    }

}

@Composable
fun NumPadRow(vararg keys: String, onClick: (String) -> Unit) {
    Row(modifier = Modifier) {
        keys.forEach {
            NumPadKey(it, onClick)
        }
    }
}