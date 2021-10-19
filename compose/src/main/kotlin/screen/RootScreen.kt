package screen

sealed class RootScreen {
    object SplashScreen: RootScreen()
    object MachineNotHomed : RootScreen()
    object MachineHomed : RootScreen()
}