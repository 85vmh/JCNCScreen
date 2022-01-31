package screen.uimodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavTab(
    val tabText: String,
    val tabImage: ImageVector,
) {
    ManualTurning("Manual", Icons.Outlined.Home),
    Conversational("Conversational", Icons.Outlined.Build),
    Programs("Programs", Icons.Outlined.List),
    Tools("Tools", Icons.Outlined.Place),
    Settings("Settings", Icons.Outlined.Settings)
}