package ui.screen.tools.root.tabs.cuttinginsert

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mindovercnc.model.*
import di.rememberScreenModel
import extensions.draggableScroll
import org.kodein.di.bindProvider
import screen.composables.DropDownClosedItem
import screen.composables.DropDownSetting
import screen.composables.DropDownView
import screen.uimodel.InputType
import ui.screen.manual.Manual
import ui.widget.ValueSetting

class AddEditCuttingInsertScreen(
    private val cuttingInsert: CuttingInsert? = null,
    private val onChanges: () -> Unit
) : Manual(
    when (cuttingInsert) {
        null -> "Add Cutting Insert"
        else -> "Edit Cutting Insert #${cuttingInsert.code}"
    }
) {

    @Composable
    override fun Actions() {
        val screenModel: AddEditCuttingInsertScreenModel = when (cuttingInsert) {
            null -> rememberScreenModel()
            else -> rememberScreenModel { bindProvider { cuttingInsert } }
        }

        val navigator = LocalNavigator.currentOrThrow
        IconButton(
            modifier = iconButtonModifier,
            onClick = {
                screenModel.applyChanges()
                onChanges.invoke()
                navigator.pop()
            }) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",
            )
        }
    }

    @Composable
    override fun Content() {
        val screenModel: AddEditCuttingInsertScreenModel = when (cuttingInsert) {
            null -> rememberScreenModel()
            else -> rememberScreenModel { bindProvider { cuttingInsert } }
        }

        val state by screenModel.state.collectAsState()
        val inputModifier = Modifier.width(80.dp)

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DropDownSetting(
                modifier = Modifier.fillMaxWidth(),
                settingName = "Made Of",
                items = state.madeOfList,
                dropDownWidth = 150.dp,
                selectedItem = state.madeOf,
                onValueChanged = screenModel::setMadeOf
            )
            state.madeOf?.let {
                if (state.isCustomGroundTool) {
                    ValueSetting(
                        settingName = "Tip Angle",
                        value = state.tipAngle.toString(),
                        inputType = InputType.TIP_ANGLE,
                        onValueChanged = {
                            val intValue = it.toIntOrNull() ?: return@ValueSetting
                            screenModel.setTipAngle(intValue)
                        },
                        inputModifier = inputModifier
                    )

                    ValueSetting(
                        settingName = "Tip Radius",
                        value = state.tipRadius.toString(),
                        inputType = InputType.TIP_RADIUS,
                        onValueChanged = {
                            val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
                            screenModel.setTipRadius(doubleValue)
                        },
                        inputModifier = inputModifier
                    )
                } else {
                    StandardInsert(
                        state,
                        insertShapeChange = screenModel::setInsertShape,
                        insertClearanceChange = screenModel::setInsertClearance,
                        toleranceClassChange = screenModel::setToleranceClass,
                        mountingChipBreakerChange = screenModel::setMountingAndChipBreaker
                    )
                    ValueSetting(
                        settingName = "Tip Radius",
                        value = state.tipRadius.toString(),
                        inputType = InputType.TIP_RADIUS,
                        onValueChanged = {
                            val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
                            screenModel.setTipRadius(doubleValue)
                        },
                        inputModifier = inputModifier
                    )
                }
                ValueSetting(
                    settingName = "Size",
                    value = state.size.toString(),
                    inputType = InputType.INSERT_SIZE,
                    onValueChanged = {
                        val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
                        screenModel.setSize(doubleValue)
                    },
                    inputModifier = inputModifier
                )
            }
        }
    }
}

@Composable
fun StandardInsert(
    state: AddEditCuttingInsertScreenModel.State,
    insertShapeChange: (InsertShape) -> Unit,
    insertClearanceChange: (InsertClearance) -> Unit,
    toleranceClassChange: (ToleranceClass) -> Unit,
    mountingChipBreakerChange: (MountingAndChipBreaker) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        InsertLetter(
            modifier = Modifier.width(50.dp),
            items = state.insertShapes,
            dropDownWidth = 50.dp,
            nothingSelectedString = "--",
            selectedItem = state.insertShape,
            onValueChanged = insertShapeChange
        ) {
            Row(
                modifier = Modifier.width(150.dp).padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${it.shape}${it.angle?.let { " $it°" } ?: ""}", style = MaterialTheme.typography.bodySmall)
            }
        }
        InsertLetter(
            modifier = Modifier.width(50.dp),
            items = state.insertClearances,
            dropDownWidth = 50.dp,
            nothingSelectedString = "--",
            selectedItem = state.insertClearance,
            onValueChanged = insertClearanceChange
        ) {
            Row(
                modifier = Modifier.width(50.dp).padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${it.angle}°", style = MaterialTheme.typography.bodySmall)
            }
        }
        InsertLetter(
            modifier = Modifier.width(50.dp),
            items = state.toleranceClasses,
            dropDownWidth = 50.dp,
            nothingSelectedString = "--",
            selectedItem = state.toleranceClass,
            onValueChanged = toleranceClassChange
        ) {
            Row(
                modifier = Modifier.width(50.dp).padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
            }
        }
        InsertLetter(
            modifier = Modifier.width(50.dp),
            items = state.mountingAndChipBreakerLists,
            dropDownWidth = 50.dp,
            nothingSelectedString = "--",
            selectedItem = state.mountingAndChipBreaker,
            onValueChanged = mountingChipBreakerChange
        ) {
            Row(
                modifier = Modifier.width(200.dp).padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(it.name, style = MaterialTheme.typography.bodyMedium)
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Mounting: ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Text(text = "${it.mountingType}", style = MaterialTheme.typography.bodySmall)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Chip Break: ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Text(text = "${it.chipBreaker}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> InsertLetter(
    items: List<T>,
    selectedItem: T?,
    dropDownWidth: Dp,
    nothingSelectedString: String,
    modifier: Modifier = Modifier,
    onValueChanged: (T) -> Unit,
    openedItemContent: @Composable (T) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    DropDownView(
        items = items,
        selected = selectedItem,
        dropDownListModifier = Modifier.draggableScroll(scrollState, scope),
        modifier = Modifier
            .width(dropDownWidth)
            .border(border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(4.dp)),
        onSelected = onValueChanged,
        closedItemContent = {
            DropDownClosedItem(
                modifier = Modifier.height(40.dp)
            ) {
                val text = when {
                    it != null -> it.toString()
                    else -> nothingSelectedString
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        openedItemContent = openedItemContent
    )
}