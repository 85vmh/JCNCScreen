package screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.CncStatusRepository
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.rememberInstance
import screen.viewmodel.NotHomedViewModel

@Composable
fun NotHomedView() {
    val viewModel: NotHomedViewModel by rememberInstance()
    val model by viewModel.uiModel.collectAsState(null)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            model?.let {
                HomedStatus("X", it.xHoming, it.xHomed)
                HomedStatus("Z", it.zHoming, it.zHomed)
            }
        }
    }
}

@Composable
fun HomedStatus(axis: String, homing: Boolean, homed: Boolean) {
    val status = when {
        homing.not() && homed.not() -> "hot homed"
        homing && homed.not() -> "-> homing in progress..."
        homing.not() && homed -> "homed"
        else -> "inconsistent state"
    }
    Text(
        text = "Axis ${axis.uppercase()} $status",
        modifier = Modifier.padding(16.dp)
    )
}