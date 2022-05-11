package ui.screen.manual.root

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import screen.composables.AxisCoordinate
import screen.composables.InputDialogView
import screen.uimodel.InputType
import ui.screen.manual.Manual
import ui.screen.manual.simplecycles.SimpleCyclesScreen
import ui.screen.manual.tapersettings.TaperSettingsScreen
import ui.screen.manual.turningsettings.TurningSettingsScreen
import ui.screen.manual.virtuallimits.VirtualLimitsScreen

class ManualTurningScreen : Manual("Manual Turning") {

    override val drawerEnabled: Boolean
        get() = true

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun DrawerContent(drawerState: DrawerState) {
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        DrawerContent {
            scope.launch {
                drawerState.close()
                navigator.push(SimpleCyclesScreen(it))
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun SheetContent(sheetState: ModalBottomSheetState) {
        val screenModel = rememberScreenModel<ManualTurningScreenModel>()
        val state by screenModel.state.collectAsState()

        state.wcsUiModel?.let { wcs ->
            Column(
                Modifier.height(180.dp).padding(top = 16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Workpiece Coordinate Systems",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(24.dp))

                WcsOffsetsView(
                    wcs = wcs,
                    modifier = Modifier.height(200.dp),
                    onOffsetClick = {
                        screenModel.setActiveWcs(it)
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Actions() {
        val screenModel = rememberScreenModel<ManualTurningScreenModel>()
        val state by screenModel.state.collectAsState()
        val scope = rememberCoroutineScope()

        val iconColor = when {
            state.virtualLimitsUiModel != null -> Color.Green
            state.virtualLimitsAvailable.not() -> Color.LightGray
            else -> LocalContentColor.current
        }

        state.wcsUiModel?.let {
            IconButton(
                modifier = iconButtonModifier,
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                }) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ) {
                            Text(
                                fontSize = 14.sp,
                                text = it.activeOffset
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "",
                    )
                }
            }
        }
        IconButton(
            enabled = state.virtualLimitsAvailable,
            modifier = iconButtonModifier,
            onClick = {
                screenModel.setVirtualLimitsActive(state.virtualLimitsUiModel == null)
            }) {
            Icon(
                tint = iconColor,
                imageVector = Icons.Default.Star,
                contentDescription = "",
            )
        }
    }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ManualTurningScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        state.numPadState?.let { numPadState ->
            InputDialogView(
                numPadState = numPadState,
                onCancel = { screenModel.closeNumPad() },
                onSubmit = {
                    numPadState.onSubmitAction(it)
                    screenModel.closeNumPad()
                }
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
        ) {

            Row(
                modifier = Modifier
                    .border(BorderStroke(0.5.dp, SolidColor(Color.DarkGray))),
                horizontalArrangement = Arrangement.End
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    AxisCoordinate(
                        state.xCoordinateUiModel,
                        isDiameterMode = true,
                        zeroPosClicked = { screenModel.setZeroPosX() },
                        absRelClicked = { screenModel.toggleXAbsRel() },
                        toolOffsetsClicked = {
                            screenModel.openNumPad(InputType.TOOL_X_COORDINATE) {
                                screenModel.setToolOffsetX(it)
                            }
                        }
                    )
                    AxisCoordinate(
                        state.zCoordinateUiModel,
                        zeroPosClicked = { screenModel.setZeroPosZ() },
                        absRelClicked = { screenModel.toggleZAbsRel() },
                        toolOffsetsClicked = {
                            screenModel.openNumPad(InputType.TOOL_Z_COORDINATE) {
                                screenModel.setToolOffsetZ(it)
                            }
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.spindleUiModel?.let {
                    SpindleStatusView(
                        uiModel = it,
                        modifier = Modifier.width(380.dp)
                            .padding(8.dp)
                            .clickable(onClick = { navigator.push(TurningSettingsScreen()) })
                    )
                }
                state.feedUiModel?.let {
                    FeedStatusView(
                        uiModel = it,
                        modifier = Modifier.width(380.dp)
                            .padding(8.dp)
                            .clickable(onClick = { navigator.push(TurningSettingsScreen()) })
                    )
                }
                Switch(
                    checked = state.taperTurningActive,
                    onCheckedChange = { screenModel.setTaperTurningActive(state.taperTurningActive.not()) }
                )
            }
            if (state.taperTurningActive) {
                TaperStatusView(
                    taperAngle = state.taperTurningAngle,
                    modifier = Modifier.width(380.dp)
                        .padding(8.dp)
                        .clickable(onClick = { navigator.push(TaperSettingsScreen()) })
                )
            }
            state.virtualLimitsUiModel?.let {
                VirtualLimitsStatusView(
                    virtualLimits = it,
                    modifier = Modifier.width(380.dp)
                        .padding(8.dp)
                        .clickable(onClick = { navigator.push(VirtualLimitsScreen()) })
                )
            }
            state.simpleCycleUiModel?.let {
                SimpleCycleStatusView(
                    null,
                    Modifier.width(380.dp)
                        .padding(8.dp)
                        .clickable(onClick = { })
                )
            }
            Row(
                modifier = Modifier.height(60.dp)
            ) {
                HandWheelStatus(state.handWheelsUiModel)
                JoystickStatus()
            }

            Button(
                onClick = {
                    screenModel.openNumPad(InputType.WORKPIECE_ZERO_COORDINATE) {
                        screenModel.setWorkpieceZ(it)
                    }
                },
            ) {
                Text("Set Workpiece Z")
            }
        }
    }
}