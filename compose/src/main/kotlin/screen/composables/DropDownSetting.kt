package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DropDownSetting(
    settingName: String,
    items: List<String>,
    dropDownWidth: Dp,
    selected: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = settingName
            )
        }
        DropDownView(
            items = items,
            selected = selected,
            modifier = Modifier
                .width(dropDownWidth)
                .border(border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(4.dp)),
            itemSelected = onValueChanged,
            closedItemContent = {
                DropDownClosedItem(it, modifier = Modifier.height(40.dp))
            },
            openedItemContent = {
                Text(it, modifier = Modifier.padding(8.dp))
            }
        )
    }
}