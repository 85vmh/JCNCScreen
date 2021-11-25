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
fun HandwheelStatus() {
    Row(
        modifier = Modifier
            .padding(8.dp),
        //.border(BorderStroke(0.5.dp, SolidColor(Color.DarkGray))),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource("handwheel.svg"),
            contentDescription = ""
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                text = "Handwheels: Active"
            )
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                text = "Increment: 0.010 mm"
            )
        }
    }
}