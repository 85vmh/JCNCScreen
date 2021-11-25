package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Preview
private fun NumPadViewPreview() {
    NumPadView(
        modifier = Modifier.fillMaxSize(),
        state = NumPadState()
    )
}

class NumPadState {
    var selectedTextState = mutableStateOf<MutableState<String>?>(null)
        private set

    fun setFieldState(state: MutableState<String>?) {
        selectedTextState.value = state
        value.value = state?.value
    }

    var value = mutableStateOf<String?>(null)
}

@Composable
fun NumPadView(
    state: NumPadState,
    modifier: Modifier = Modifier,
) {
    var numPadValue by state.value
    val enabled = numPadValue != null
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(numPadValue ?: "")

        NumPadRow("1", "2", "3", enabled = enabled) { if (numPadValue != null) numPadValue += it }
        NumPadRow("4", "5", "6", enabled = enabled) { if (numPadValue != null) numPadValue += it }
        NumPadRow("7", "8", "9", enabled = enabled) { if (numPadValue != null) numPadValue += it }
        NumPadRow("+/-", "0", ".", enabled = enabled) { if (numPadValue != null) numPadValue += it }

        Button(
            enabled = enabled,
            onClick = {
                numPadValue?.let {
                    state.selectedTextState.value?.value = it
                }
                state.setFieldState(null)
            }
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun NumPadKey(key: String, enabled: Boolean, onClick: (String) -> Unit) {
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
fun NumPadRow(vararg keys: String, enabled: Boolean, onClick: (String) -> Unit) {
    Row(modifier = Modifier) {
        keys.forEach {
            NumPadKey(it, enabled, onClick)
        }
    }
}