package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropDownView(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    itemSelected: (T) -> Unit,
    closedItemContent: @Composable (T) -> Unit,
    openedItemContent: @Composable (T) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) } // initial value

    Surface(
        modifier = modifier,
        onClick = {
            isOpen = true
        }
    ) {
        closedItemContent(selected)
        DropDownList(
            modifier = Modifier,
            requestToOpen = isOpen,
            list = items,
            onDismiss = { isOpen = false },
            onSelected = itemSelected,
            itemContent = openedItemContent
        )
    }
}

@Composable
fun DropDownClosedItem(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "")
    }
}

@Composable
private fun <T> DropDownList(
    modifier: Modifier = Modifier,
    requestToOpen: Boolean = false,
    list: List<T>,
    onDismiss: () -> Unit,
    onSelected: (T) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = requestToOpen,
        onDismissRequest = onDismiss,
    ) {
        list.forEach { content ->
            DropdownMenuItem(
                modifier = modifier,
                onClick = {
                    onDismiss()
                    onSelected(content)
                }
            ) {
                itemContent(content)
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    DropDownClosedItem("A Test", Modifier.size(200.dp))
}