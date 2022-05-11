package ui.tab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.TabOptions
import ui.screen.manual.Manual
import ui.screen.manual.root.ManualTurningScreen

object ManualTab : AppTab<Manual>(ManualTurningScreen()) {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Manual"
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}