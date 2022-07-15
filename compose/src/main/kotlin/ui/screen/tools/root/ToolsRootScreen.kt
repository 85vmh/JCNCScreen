package ui.screen.tools.root

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.rememberScreenModel
import screen.composables.TabbedContentView
import screen.composables.ToolsTabItem
import ui.screen.tools.Tools
import ui.screen.tools.root.tabs.lathetool.AddEditLatheToolScreen
import ui.screen.tools.root.tabs.toolholder.AddEditHolderScreen
import ui.screen.tools.root.tabs.CuttingInsertsContent
import ui.screen.tools.root.tabs.LatheToolsContent
import ui.screen.tools.root.tabs.ToolHoldersContent

private val tabContentModifier = Modifier.fillMaxWidth()

class ToolsRootScreen : Tools("Tools") {

    @Composable
    override fun Fab() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel<ToolsScreenModel>()
        val state by screenModel.state.collectAsState()

        when (state.currentTabIndex) {
            0 -> ExtendedFloatingActionButton(
                text = { Text("New Holder") },
                onClick = {
                    navigator.push(AddEditHolderScreen {
                        screenModel.loadToolHolders()
                    })
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
                    navigator.push(AddEditLatheToolScreen {
                        screenModel.loadLatheTools()
                    })
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
            tabs = ToolsTabItem.values(),
            currentTabIndex = state.currentTabIndex,
            onTabSelected = screenModel::selectTabWithIndex,
            text = { Text(it.tabTitle) }
        ) {
            when (it) {
                ToolsTabItem.ToolHolders -> ToolHoldersContent(
                    state = state,
                    onDelete = screenModel::requestDeleteToolHolder,
                    onLoad = screenModel::loadToolHolder,
                    onHolderChanged = screenModel::loadToolHolders,
                    modifier = tabContentModifier
                )
                ToolsTabItem.LatheTools -> LatheToolsContent(
                    state,
                    onDelete = screenModel::requestDeleteLatheTool,
                    onToolChanged = screenModel::loadLatheTools,
                    modifier = tabContentModifier
                )
                ToolsTabItem.CuttingInserts -> CuttingInsertsContent(
                    state,
                    onEdit = screenModel::editCuttingInsert,
                    onDelete = screenModel::deleteCuttingInsert,
                    modifier = tabContentModifier
                )
            }
        }

        state.toolHolderDeleteModel?.let {
            ToolHolderDeleteDialog(
                deleteModel = it,
                deleteClick = screenModel::deleteToolHolder,
                abortClick = screenModel::cancelDeleteToolHolder
            )
        }
        state.latheToolDeleteModel?.let {
            LatheToolDeleteDialog(
                deleteModel = it,
                deleteClick = screenModel::deleteLatheTool,
                abortClick = screenModel::cancelDeleteLatheTool
            )
        }
    }
}