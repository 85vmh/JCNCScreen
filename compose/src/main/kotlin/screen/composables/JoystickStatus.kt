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
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalUnitApi::class)
@Composable
fun JoystickStatus() {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource("joystick.svg"),
            contentDescription = ""
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                fontSize = TextUnit(14f, TextUnitType.Sp),
                fontWeight = FontWeight.Normal,
                text = "Joystick: Rapid Only"
            )
            Text(
                fontSize = TextUnit(14f, TextUnitType.Sp),
                fontWeight = FontWeight.Normal,
                text = "Rapid speed: 3000 mm/min"
            )
        }
    }
}