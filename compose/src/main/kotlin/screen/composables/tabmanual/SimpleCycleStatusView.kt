package screen.composables.tabmanual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import usecase.SimpleCyclesUseCase
import usecase.model.SimpleCycleStatus

@Composable
fun SimpleCycleStatusView(modifier: Modifier = Modifier) {
    val useCase: SimpleCyclesUseCase by rememberInstance()

    val subToCall by useCase.subroutineToCall.collectAsState()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
        elevation = 16.dp
    ) {

        Column {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium,
                text = "Simple Cycle"
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.DarkGray,
                thickness = 1.dp
            )
            Text(text = subToCall, textAlign = TextAlign.Center)
        }
    }
}