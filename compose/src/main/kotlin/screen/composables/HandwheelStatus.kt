package screen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.kodein.di.compose.rememberInstance
import usecase.ManualTurningUseCase

@Composable
fun HandwheelStatus() {
    val useCase: ManualTurningUseCase by rememberInstance()
    val state by useCase.handwheelsState.collectAsState(null)

    val activeStatus = state?.let {
        if (it.active && it.increment > 0) "Active" else "Inactive"
    } ?: "Inactive"

    val increment = state?.let {
        "${it.increment} ${it.unit}"
    }

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
                text = "Handwheels: $activeStatus"
            )
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                text = "Increment: $increment"
            )
        }
    }
}