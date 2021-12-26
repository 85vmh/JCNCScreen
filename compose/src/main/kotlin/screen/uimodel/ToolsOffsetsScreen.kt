package screen.uimodel

sealed class ToolsOffsetsScreen(
    title: String,
    val previousScreen: ToolsOffsetsScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.ToolsOffsets) {

    object ToolsOffsetsRootScreen : ToolsOffsetsScreen(
        title = "Tools & Offsets",
    )

    class AddEditToolScreen(
        previousScreen: ToolsOffsetsScreen?
    ) : ToolsOffsetsScreen(
        title = "Add/Edit Tool",
        previousScreen = previousScreen
    )
}