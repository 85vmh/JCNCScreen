package screen.composables.tabtools

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindovercnc.base.data.LatheTool
import extensions.draggableScroll
import extensions.toFixedDigitsString
import navigation.AppNavigator
import org.kodein.di.compose.rememberInstance
import screen.composables.LabelWithValue
import screen.composables.VerticalDivider
import screen.composables.common.AppTheme
import screen.composables.platform.VerticalScrollbar
import screen.uimodel.ToolsOffsetsScreen
import usecase.ToolsUseCase

enum class FabState {
    Expanded, Collapsed
}

@Composable
fun ExpandableFab(
    modifier: Modifier = Modifier,
    fabState: FabState,
    onFabStateChange: (FabState) -> Unit,
    onChildFabClicked: (ChildFabItem) -> Unit
) {
    val transition = updateTransition(targetState = fabState)

    val rotate by transition.animateFloat {
        if (it == FabState.Expanded) 90f else 0f
    }

    val fabScale by transition.animateFloat {
        if (it == FabState.Expanded) 36f else 0f
    }

    val alpha by transition.animateFloat(transitionSpec = { tween(durationMillis = 50) }) {
        if (it == FabState.Expanded) 1f else 0f
    }

    val textShadow by transition.animateDp(transitionSpec = { tween(durationMillis = 50) }) {
        if (it == FabState.Expanded) 2.dp else 0.dp
    }

    Column(
        modifier = modifier.padding(vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (transition.currentState == FabState.Expanded) {
            ChildFab(
                item = ChildFabItem.ToolOffset,
                onChildFabItemClicked = onChildFabClicked,
                alpha = alpha,
                textShadow = textShadow,
                fabScale = fabScale
            )
            Spacer(modifier = Modifier.size(32.dp))
            ChildFab(
                item = ChildFabItem.WorkOffset,
                onChildFabItemClicked = onChildFabClicked,
                alpha = alpha,
                textShadow = textShadow,
                fabScale = fabScale
            )
        }

        ExtendedFloatingActionButton(
            onClick = {
                onFabStateChange(
                    if (transition.currentState == FabState.Expanded) {
                        FabState.Collapsed
                    } else {
                        FabState.Expanded
                    }
                )
            },
            text = { Text("Set Offsets") },
            icon = {
                Image(
                    //bitmap = useResource("center.png") { loadImageBitmap(it) },
                    imageVector = Icons.Filled.Star,
                    contentDescription = "",
                    modifier = Modifier.rotate(rotate)
                )
            }
        )
    }
}

enum class ChildFabItem(val icon: ImageBitmap, val label: String) {
    WorkOffset(useResource("center.png") { loadImageBitmap(it) }, "Work Offset"),
    ToolOffset(useResource("center.png") { loadImageBitmap(it) }, "Tool Offset")
}

@Composable
fun ChildFab(
    item: ChildFabItem,
    alpha: Float,
    textShadow: Dp,
    fabScale: Float,
    onChildFabItemClicked: (ChildFabItem) -> Unit
) {
    val buttonColor = AppTheme.colors.material.secondary
    val shadowColor = Color.DarkGray.copy(alpha = .5f)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier.size(24.dp).clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(
                    bounded = false,
                    radius = 20.dp,
                    color = AppTheme.colors.material.onSurface
                ),
                onClick = { onChildFabItemClicked.invoke(item) }
            )
        ) {
            drawCircle(
                color = shadowColor,
                radius = fabScale,
                center = Offset(
                    center.x + 2f,
                    center.y + 2f
                )
            )

            drawCircle(
                color = buttonColor,
                radius = fabScale
            )

            drawImage(
                image = item.icon, topLeft = Offset(
                    center.x - (item.icon.width / 2),
                    center.y - (item.icon.height / 2)
                ),
                alpha = alpha
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = item.label,
            modifier = Modifier
                .alpha(
                    animateFloatAsState(
                        targetValue = alpha,
                        animationSpec = tween(50)
                    ).value
                )
                .shadow(textShadow)
                .background(AppTheme.colors.backgroundLight)
                .padding(8.dp)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ToolLibraryView(
    modifier: Modifier
) {
    val useCase: ToolsUseCase by rememberInstance()
    val scope = rememberCoroutineScope()
    val toolsList by useCase.getTools().collectAsState(emptyList())
    val currentToolNo by useCase.getCurrentToolNo().collectAsState(null)
    val currentTool by useCase.getCurrentTool().collectAsState(null)
    val appNavigator by rememberInstance<AppNavigator>()
    val currentScreen by appNavigator.currentScreen.collectAsState(null)

    var fabState by remember { mutableStateOf(FabState.Collapsed) }

    var toolToDelete by remember { mutableStateOf(0) }

    if (toolToDelete > 0) {
        AlertDialog(
            onDismissRequest = { toolToDelete = 0 },
            title = {
                Text(text = "Confirmation", fontWeight = FontWeight.Bold)
            },
            text = {
                Row(
                    modifier = Modifier.width(250.dp)
                ) {
                    Text("Delete tool $toolToDelete ?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        useCase.deleteTool(toolToDelete)
                        toolToDelete = 0
                    }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        toolToDelete = 0
                    }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            stickyHeader {
                ToolsHeader()
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
            items(toolsList) { item ->
                ToolRow(
                    item = item,
                    isCurrent = item.toolNo == currentToolNo,
                    onEditClicked = {
                        currentScreen?.let { screen ->
                            appNavigator.navigate(
                                ToolsOffsetsScreen.AddEditToolScreen(
                                    previousScreen = screen as ToolsOffsetsScreen,
                                    latheTool = it
                                )
                            )
                        }
                    },
                    onDeleteClicked = {
                        toolToDelete = it.toolNo
                    },
                    onLoadClicked = {
                        useCase.loadTool(it.toolNo)
                    }
                )
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }

        currentTool?.let { latheTool ->
            ExpandableFab(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 70.dp, bottom = 50.dp),
                fabState = fabState,
                onFabStateChange = { fabState = it }
            ) {
                when (it) {
                    ChildFabItem.ToolOffset -> {
                        currentScreen?.let { screen ->
                            appNavigator.navigate(
                                ToolsOffsetsScreen.ToolOffsetsScreen(
                                    previousScreen = screen as ToolsOffsetsScreen,
                                    latheTool = latheTool
                                )
                            )
                        }
                    }
                    ChildFabItem.WorkOffset -> {
                        currentScreen?.let { screen ->
                            appNavigator.navigate(
                                ToolsOffsetsScreen.WorkOffsetsScreen(
                                    previousScreen = screen as ToolsOffsetsScreen,
                                    latheTool = latheTool
                                )
                            )
                        }
                    }
                }
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd).width(30.dp),
            scrollState,
            toolsList.size,
            60.dp
        )
    }
}

private enum class ToolsColumnModifier(val modifier: Modifier) {
    ToolNo(Modifier.width(50.dp)),
    Offset(Modifier.width(130.dp)),
    Wear(Modifier.width(130.dp)),
    TipRadius(Modifier.width(100.dp)),
    Orientation(Modifier.width(110.dp)),
}

@Composable
fun ToolsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = ToolsColumnModifier.ToolNo.modifier,
            textAlign = TextAlign.Center,
            text = "Tool"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Offset.modifier,
            textAlign = TextAlign.Center,
            text = "Offset"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Wear.modifier,
            textAlign = TextAlign.Center,
            text = "Wear"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.TipRadius.modifier,
            textAlign = TextAlign.Center,
            text = "Tip Radius"
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Orientation.modifier,
            textAlign = TextAlign.Center,
            text = "Orientation"
        )
        VerticalDivider()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolRow(
    item: LatheTool,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
    onEditClicked: (LatheTool) -> Unit,
    onDeleteClicked: (LatheTool) -> Unit,
    onLoadClicked: (LatheTool) -> Unit
) {
    val nonSelectedModifier = modifier.height(60.dp)
    val selectedModifier = nonSelectedModifier.border(BorderStroke(1.dp, Color.Blue))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = if (isCurrent) selectedModifier else nonSelectedModifier
//            .combinedClickable(
//                onClick = {
//                    println("--Clicked")
//                },
//                onDoubleClick = {
//                    println("--Double Clicked")
//                },
//                onLongClick = {
//                    println("--Long Clicked")
//                }
//            )
    ) {
        Text(
            modifier = ToolsColumnModifier.ToolNo.modifier,
            textAlign = TextAlign.Center,
            text = item.toolNo.toString()
        )
        VerticalDivider()
        Column(
            modifier = ToolsColumnModifier.Offset.modifier
        ) {
            LabelWithValue("X:", item.xOffset.toFixedDigitsString())
            LabelWithValue("Z:", item.zOffset.toFixedDigitsString())
        }
        VerticalDivider()
        Column(
            modifier = ToolsColumnModifier.Offset.modifier
        ) {
            LabelWithValue("X:", item.xWear.toFixedDigitsString())
            LabelWithValue("Z:", item.zWear.toFixedDigitsString())
        }
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.TipRadius.modifier,
            textAlign = TextAlign.Center,
            text = item.tipRadius.toFixedDigitsString()
        )
        VerticalDivider()
        Text(
            modifier = ToolsColumnModifier.Orientation.modifier,
            textAlign = TextAlign.Center,
            text = item.orientation.angle.toString()
        )
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = {
                onEditClicked.invoke(item)
            }) {
            Icon(Icons.Default.Edit, contentDescription = "")
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            enabled = isCurrent.not(),
            onClick = {
                onDeleteClicked.invoke(item)
            }) {
            Icon(Icons.Default.Delete, contentDescription = "")
        }
        VerticalDivider()
        IconButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = {
                onLoadClicked.invoke(item)
            }) {
            Icon(Icons.Default.ExitToApp, contentDescription = "")
        }
        VerticalDivider()
    }
}

