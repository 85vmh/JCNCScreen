package screen.uimodel

sealed class ProgramsScreen(
    title: String,
    val previousScreen: ProgramsScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Programs) {

    object ProgramsRootScreen : ProgramsScreen(
        title = "Run Programs",
    )

    class ProgramLoadedScreen(
        previousScreen: ProgramsScreen?
    ) : ProgramsScreen(
        title = "Program Loaded",
        previousScreen = previousScreen
    )
}