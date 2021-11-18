package screen

sealed class BaseScreen {
    object SplashScreen : BaseScreen()
    object NotHomedScreen : BaseScreen()
    object RootScreen : BaseScreen()
    object TurningSettingsScreen : BaseScreen()
}