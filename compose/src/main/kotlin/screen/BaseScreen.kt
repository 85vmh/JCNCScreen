package screen

sealed class BaseScreen {
    object SplashScreen : BaseScreen()
    object NotHomedScreen : BaseScreen()
    object RootScreen : BaseScreen()
    object TurningSettingsScreen : BaseScreen()
    object ToolLibraryScreen : BaseScreen()
    object G5xOffsetsScreen : BaseScreen()
    object ConversationalScreen : BaseScreen()
    object ProgramsScreen : BaseScreen()
}