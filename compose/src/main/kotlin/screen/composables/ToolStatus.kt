package screen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun ToolStatus() {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource("lathe-tool.png"),
            contentDescription = ""
        )
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                text = "Current Tool #: 1"
            )
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                text = "Orientation: Front-Left"
            )
        }
    }
}