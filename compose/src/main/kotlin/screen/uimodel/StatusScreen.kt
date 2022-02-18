package screen.uimodel

sealed class StatusScreen(
    title: String,
    val previousScreen: StatusScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Status) {

    object StatusRootScreen : StatusScreen(
        title = "Status",
    )

    class EditOffsetSettings(
        previousScreen: StatusScreen?
    ) : StatusScreen(
        title = "Edit Offset",
        previousScreen = previousScreen
    )
}