package screen.uimodel

import screen.composables.ConversationalOperation

sealed class ConversationalScreen(
    title: String,
    val previousScreen: ConversationalScreen? = null,
) : TabScreen(title, previousScreen != null, BottomNavTab.Conversational) {

    object ConversationalRootScreen : ConversationalScreen(
        title = "Conversational Turning",
    )

    class NewOperationScreen(
        val conversationalOperation: ConversationalOperation,
        previousScreen: ConversationalScreen?
    ) : ConversationalScreen(
        title = conversationalOperation.displayableString,
        previousScreen = previousScreen
    )
}