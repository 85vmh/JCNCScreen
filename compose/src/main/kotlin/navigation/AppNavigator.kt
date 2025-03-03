package navigation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import screen.uimodel.*


class AppNavigator {
    private val allTabs = listOf(
        BottomNavTab.ManualTurning,
        BottomNavTab.Conversational,
        BottomNavTab.Programs,
        BottomNavTab.ToolsOffsets,
        BottomNavTab.Status
    )

    private val _previousTab = MutableStateFlow(BottomNavTab.ManualTurning)
    private val _currentTab = MutableStateFlow(BottomNavTab.ManualTurning)

    private val _manualScreen = MutableStateFlow<ManualScreen>(ManualScreen.ManualRootScreen)
    private val _conversationalScreen = MutableStateFlow<ConversationalScreen>(ConversationalScreen.ConversationalRootScreen)
    private val _programsScreen = MutableStateFlow<ProgramsScreen>(ProgramsScreen.ProgramsRootScreen)
    private val _toolsOffsetsScreen = MutableStateFlow<ToolsOffsetsScreen>(ToolsOffsetsScreen.ToolsRootScreen)
    private val _statusScreen = MutableStateFlow<StatusScreen>(StatusScreen.StatusRootScreen)
    private val _enabledTabs = MutableStateFlow(allTabs)

    val currentTab = _currentTab.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentScreen = _currentTab.flatMapLatest {
        when (it) {
            BottomNavTab.ManualTurning -> _manualScreen
            BottomNavTab.Conversational -> _conversationalScreen
            BottomNavTab.Programs -> _programsScreen
            BottomNavTab.ToolsOffsets -> _toolsOffsetsScreen
            BottomNavTab.Status -> _statusScreen
        }
    }

    val enabledTabs = _enabledTabs.asStateFlow()

    fun selectTab(tab: BottomNavTab) {
        _currentTab.value = tab
    }

    fun setReady(isReady: Boolean) {
        when {
            isReady -> {
                _enabledTabs.value = allTabs
                if (_currentTab.value == BottomNavTab.Status) {
                    selectTab(_previousTab.value)
                }
            }
            else -> {
                _previousTab.value = _currentTab.value
                _enabledTabs.value = listOf(BottomNavTab.Status)
                selectTab(BottomNavTab.Status)
            }
        }
    }

    fun navigate(to: TabScreen) {
        when (to) {
            is ManualScreen -> _manualScreen.value = to
            is ConversationalScreen -> _conversationalScreen.value = to
            is ProgramsScreen -> _programsScreen.value = to
            is ToolsOffsetsScreen -> _toolsOffsetsScreen.value = to
            is StatusScreen -> _statusScreen.value = to
        }
        _currentTab.value = to.tab
    }

    fun navigateUp() {
        when (_currentTab.value) {
            BottomNavTab.ManualTurning -> {
                _manualScreen.update {
                    it.previousScreen ?: it
                }
            }
            BottomNavTab.Conversational -> {
                _conversationalScreen.update {
                    it.previousScreen ?: it
                }
            }
            BottomNavTab.Programs -> {
                _programsScreen.update {
                    it.previousScreen ?: it
                }
            }
            BottomNavTab.ToolsOffsets -> {
                _toolsOffsetsScreen.update {
                    it.previousScreen ?: it
                }
            }
            BottomNavTab.Status -> {
                _statusScreen.update {
                    it.previousScreen ?: it
                }
            }
        }
    }
}