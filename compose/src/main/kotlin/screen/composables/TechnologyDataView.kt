package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TechnologyDataState(
    offset: String,
    cutDirection: String,
    operationType: String,
) {
    val workOffset = mutableStateOf(offset)
    val cutDirection = mutableStateOf(cutDirection)
    val operationType = mutableStateOf(operationType)
}

@Composable
fun TechnologyDataView() {
    val workOffsets = remember { listOf("G54", "G55", "G56", "G57", "G58", "G59", "G59.1", "G59.2", "G59.3") }
    val cutDirection = remember { listOf("Longitudinal", "Transversal") }
    val operationType = remember { listOf("Roughing", "Finishing", "Roughing & Finishing") }

    val state = remember { TechnologyDataState(workOffsets.first(), cutDirection.first(), operationType.first()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row {
            DropDownSetting(
                modifier = Modifier.weight(1f),
                settingName = "Work Offset",
                items = workOffsets,
                dropDownWidth = 90.dp,
                selected = state.workOffset.value,
                onValueChanged = {
                    state.workOffset.value = it
                }
            )
            DropDownSetting(
                modifier = Modifier.weight(1f),
                settingName = "Cut Direction",
                items = cutDirection,
                dropDownWidth = 150.dp,
                selected = state.cutDirection.value,
                onValueChanged = {
                    state.cutDirection.value = it
                }
            )
        }
        Row {
            InputSetting(
                modifier = Modifier.weight(1f),
                settingName = "Tool Clearance",
                active = true,
                value = "1",
                units = "mm"
            ) {}
            InputSetting(
                modifier = Modifier.weight(1f),
                settingName = "Max Spindle Speed",
                active = true,
                value = "2000",
                units = "rev/min"
            ) {}
        }

        DropDownSetting("Operation Type", operationType, 220.dp, state.operationType.value, onValueChanged = {
            state.operationType.value = it
        })
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardWithTitle(
                cardTitle = "Roughing Parameters",
                titleSize = 16.sp,
                modifier = Modifier.weight(1f),
                contentSpacing = 16.dp,
                cardCornerRadius = 4.dp,
                cardElevation = 8.dp,
                borderColor = Color.LightGray
            ) {
                InputSetting("Tool Number", true, "1", "") {}
                InputSetting("Surface Speed", true, "200", "m/min") {}
                InputSetting("Feed Rate", true, "0.2", "mm/rev") {}
                InputSetting("Depth of Cut", true, "2", "mm") {}
            }
            CardWithTitle(
                cardTitle = "Finishing Parameters",
                titleSize = 16.sp,
                modifier = Modifier.weight(1f),
                contentSpacing = 16.dp,
                cardCornerRadius = 4.dp,
                cardElevation = 8.dp,
                borderColor = Color.LightGray
            ) {
                InputSetting("Tool Number", true, "1") {}
                InputSetting("Surface Speed", true, "200", "m/min") {}
                InputSetting("Feed Rate", true, "0.2", "mm/rev") {}
                InputSetting("Depth of Cut", true, "2", "mm") {}
            }
        }
    }
}


@Composable
fun Container(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(Color.LightGray)),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content.invoke()
        }
    }
}

@Composable
fun DropDownSetting(
    settingName: String,
    items: List<String>,
    dropDownWidth: Dp,
    selected: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = settingName
            )
        }
        DropDownView(
            items = items,
            selected = selected,
            modifier = Modifier
                .width(dropDownWidth)
                .border(border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(4.dp)),
            itemSelected = onValueChanged,
            closedItemContent = {
                DropDownClosedItem(it, modifier = Modifier.height(40.dp))
            },
            openedItemContent = {
                Text(it, modifier = Modifier.padding(8.dp))
            }
        )
    }
}

@Composable
fun InputSetting(
    settingName: String,
    active: Boolean,
    value: String,
    units: String? = null,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    Row(
        verticalAlignment = alignment,
        modifier = modifier.padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = settingName
            )
        }
        ValueAndUnit(value, units, active, alignment, modifier = Modifier.width(170.dp)) {
            onValueChanged(it)
        }
    }
}

