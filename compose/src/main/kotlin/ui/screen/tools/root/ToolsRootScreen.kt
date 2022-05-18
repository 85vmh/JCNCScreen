package ui.screen.tools.root

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import screen.composables.TabItem
import screen.composables.TabbedContentView
import ui.screen.tools.Tools
import ui.screen.tools.addholder.AddEditHolderScreen
import ui.screen.tools.root.tabs.CuttingInsertsContent
import ui.screen.tools.root.tabs.LatheToolsContent
import ui.screen.tools.root.tabs.ToolHoldersContent

class ToolsRootScreen : Tools("Tools") {

    @Composable
    override fun Fab() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel<ToolsScreenModel>()
        val state by screenModel.state.collectAsState()

        when(state.currentTabIndex){
            0 -> ExtendedFloatingActionButton(
                text = { Text("New Holder") },
                onClick = {
                    navigator.push(AddEditHolderScreen())
                },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                    )
                }
            )
            1 -> ExtendedFloatingActionButton(
                text = { Text("New Tool") },
                onClick = {
                    //navigator.push(AddEditHolderScreen())
                },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                    )
                }
            )
            2 -> ExtendedFloatingActionButton(
                text = { Text("New Insert") },
                onClick = {
                    //navigator.push(AddEditHolderScreen())
                },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                    )
                }
            )
        }


    }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ToolsScreenModel>()
        val state by screenModel.state.collectAsState()

        TabbedContentView(
            tabs = listOf(
                TabItem("Tool Holders") { ToolHoldersContent(screenModel) },
                TabItem("Lathe Tools") { LatheToolsContent(screenModel) },
                TabItem("Cutting Inserts") { CuttingInsertsContent(screenModel) },
            ),
            currentTabIndex = state.currentTabIndex,
            onTabSelected = screenModel::selectTabWithIndex
        )
    }
}