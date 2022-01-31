package screen

sealed class BaseScreen {
    object NotHomedScreen : BaseScreen()
    object HomedTabbedScreen : BaseScreen()
}