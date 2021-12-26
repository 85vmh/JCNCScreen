package screen.uimodel

import screen.composables.TurningSettingsViewModel

sealed class ManualScreen(
    title: String,
    val previousScreen: ManualScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.ManualTurning) {

    object ManualRootScreen : ManualScreen(
        title = "Manual Turning",
    )

    class TurningSettingsScreen(
        val viewModel: TurningSettingsViewModel,
        previousScreen: ManualScreen?
    ) : ManualScreen(
        title = "Turning Settings",
        previousScreen = previousScreen
    )
}