package screen.uimodel

sealed class ConversationalScreen(
    title: String,
    val previousScreen: ConversationalScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Conversational) {

    object ConversationalRootScreen : ConversationalScreen(
        title = "Conversational Turning",
    )

    class NewOperationScreen(
        previousScreen: ConversationalScreen?
    ) : ConversationalScreen(
        title = "New Operation",
        previousScreen = previousScreen
    )
}