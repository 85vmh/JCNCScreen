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
    ToolsOffsets("Tools & Offsets", Icons.Outlined.Place),
    Status("Status", Icons.Outlined.Info)
}