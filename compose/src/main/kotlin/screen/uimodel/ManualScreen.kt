package screen.uimodel

import screen.composables.tabmanual.LimitsSettingsViewModel
import screen.composables.tabmanual.TaperSettingsViewModel
import screen.composables.tabmanual.TurningSettingsViewModel


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

    class TaperSettingsScreen(
        val viewModel: TaperSettingsViewModel,
        previousScreen: ManualScreen?
    ) : ManualScreen(
        title = "Taper Settings",
        previousScreen = previousScreen
    )

    class LimitsSettingsScreen(
        val viewModel: LimitsSettingsViewModel,
        previousScreen: ManualScreen?
    ) : ManualScreen(
        title = "Virtual Limits",
        previousScreen = previousScreen
    )
}