package screen.uimodel

sealed class ToolsScreen(
    title: String,
    val previousScreen: ToolsScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Tools) {

    object ToolsRootScreen : ToolsScreen(
        title = "Tool Library",
    )

    class AddEditToolScreen(
        previousScreen: ToolsScreen?
    ) : ToolsScreen(
        title = "Add/Edit Tool",
        previousScreen = previousScreen
    )
}