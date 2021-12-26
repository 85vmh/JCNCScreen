package screen

sealed class BaseScreen {
    object NotHomedScreen : BaseScreen()
    object ManualTurningScreen : BaseScreen()
    object ConversationalScreen : BaseScreen()
    object ProgramsScreen : BaseScreen()
    object ToolLibraryScreen : BaseScreen()
    object TurningSettingsScreen : BaseScreen()
    object G5xOffsetsScreen : BaseScreen()
}