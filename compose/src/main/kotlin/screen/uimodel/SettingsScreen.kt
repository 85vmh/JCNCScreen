package screen.uimodel

sealed class SettingsScreen(
    title: String,
    val previousScreen: SettingsScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Settings) {

    object SettingsRootScreen : SettingsScreen(
        title = "Lathe Settings",
    )

    class EditOffsetSettings(
        previousScreen: SettingsScreen?
    ) : SettingsScreen(
        title = "Edit Offset",
        previousScreen = previousScreen
    )
}