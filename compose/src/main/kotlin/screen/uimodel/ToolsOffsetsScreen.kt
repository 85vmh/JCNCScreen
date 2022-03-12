package screen.uimodel

import com.mindovercnc.base.data.LatheTool

sealed class ToolsOffsetsScreen(
    title: String,
    val previousScreen: ToolsOffsetsScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.ToolsOffsets) {

    object ToolsRootScreen : ToolsOffsetsScreen(
        title = "Tool Library",
    )

    class AddEditToolScreen(
        previousScreen: ToolsOffsetsScreen?,
        latheTool: LatheTool? = null
    ) : ToolsOffsetsScreen(
        title = latheTool?.let {
            "Edit Tool #${it.toolNo}"
        } ?: "Add new Tool",
        previousScreen = previousScreen
    )

    class ToolOffsetsScreen(
        previousScreen: ToolsOffsetsScreen?,
        latheTool: LatheTool
    ) : ToolsOffsetsScreen(
        title = "Tool #${latheTool.toolNo} Offsets",
        previousScreen = previousScreen
    )

    class WorkOffsetsScreen(
        previousScreen: ToolsOffsetsScreen?,
        latheTool: LatheTool
    ) : ToolsOffsetsScreen(
        title = "Work Offsets",
        previousScreen = previousScreen
    )
}