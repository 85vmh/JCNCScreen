package screen.composables.tabstatus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusView(modifier: Modifier) {
    Column {
        OffsetsView(Modifier)
        Divider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
        MessagesView(Modifier)
    }
}