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
import usecase.ToolsUseCase

@Composable
fun ToolStatus() {
    val useCase: ToolsUseCase by rememberInstance()
    val currentTool by useCase.getCurrentTool().collectAsState(null)

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource("lathe-tool.png"),
            contentDescription = ""
        )
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            currentTool?.let {
                Text(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    text = "Current Tool #: ${it.toolNo}"
                )
                Text(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    text = "Orientation: ${it.orientation.orient}"
                )
            } ?: run {
                Text(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    text = "No tool loaded!"
                )
            }
        }
    }
}