package screen.uimodel

import screen.viewmodel.CycleParametersViewModel
import screen.viewmodel.LimitsSettingsViewModel
import screen.viewmodel.TaperSettingsViewModel
import screen.viewmodel.TurningSettingsViewModel


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

    class SimpleCyclesScreen(
        previousScreen: ManualScreen?
    ) : ManualScreen(
        title = "Simple Cycles",
        previousScreen = previousScreen
    )

    class CycleParametersScreen(
        val viewModel: CycleParametersViewModel,
        simpleCycle: SimpleCycle,
        previousScreen: ManualScreen?,
    ) : ManualScreen(
        title = simpleCycle.displayableString,
        previousScreen = previousScreen
    ){
        fun exitEditMode() {
            viewModel.exitEditMode()
        }
    }

    class LimitsSettingsScreen(
        val viewModel: LimitsSettingsViewModel,
        previousScreen: ManualScreen?
    ) : ManualScreen(
        title = "Virtual Limits",
        previousScreen = previousScreen
    ) {
        fun exitEditMode() {
            viewModel.exitEditMode()
        }
    }
}