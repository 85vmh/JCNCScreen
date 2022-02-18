package screen.uimodel

import com.mindovercnc.base.data.LatheTool

sealed class ToolsScreen(
    title: String,
    val previousScreen: ToolsScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Tools) {

    object ToolsRootScreen : ToolsScreen(
        title = "Tool Library",
    )

    class AddEditToolScreen(
        previousScreen: ToolsScreen?,
        latheTool: LatheTool? = null
    ) : ToolsScreen(
        title = latheTool?.let {
            "Edit Tool #${it.toolNo}"
        } ?: "Add new Tool",
        previousScreen = previousScreen
    )
}