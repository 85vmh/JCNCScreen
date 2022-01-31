package screen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LabelWithValue(
    label: String,
    value: String,
    paddingStart: Dp = 16.dp,
    paddingEnd: Dp = 16.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = paddingStart),
            text = label
        )
        Text(
            modifier = Modifier.padding(end = paddingEnd),
            text = value
        )
    }
}