package screen.composables.tabconversational

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.CuttingParametersView
import screen.composables.DropDownSetting
import screen.composables.tabmanual.NumericInputWithUnit
import screen.uimodel.*
import usecase.ConversationalUseCase
import usecase.model.CuttingParametersState

@Composable
fun TurningTechnologyDataView() {

    val useCase: ConversationalUseCase by rememberInstance()

    val odTurningDataState = remember { useCase.getOdTurningDataState() }
//    val roughingState = remember { CuttingParametersState(1, 200, 0.100, 2.500) }
//    val finishState = remember { CuttingParametersState(1, 200, 0.05, 0.500) }

    Box(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.width(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                DropDownSetting(
                    modifier = Modifier.width(400.dp),
                    settingName = "Work Offset",
                    items = Wcs.values().map { it.text },
                    dropDownWidth = 90.dp,
                    selected = odTurningDataState.wcs.value.text,
                    onValueChanged = {
                        odTurningDataState.wcs.value = Wcs.fromString(it)!!
                    }
                )
                DropDownSetting(
                    modifier = Modifier.width(400.dp),
                    settingName = "Material",
                    items = WorkpieceMaterial.values().map { it.text },
                    dropDownWidth = 180.dp,
                    selected = odTurningDataState.material.value.name,
                    onValueChanged = {
                        odTurningDataState.material.value = WorkpieceMaterial.fromString(it)!!
                    }
                )
                DropDownSetting(
                    modifier = Modifier.width(400.dp),
                    settingName = "Cut Direction",
                    items = CutDirection.values().map { it.name },
                    dropDownWidth = 150.dp,
                    selected = odTurningDataState.cutDirection.value.name,
                    onValueChanged = {
                        odTurningDataState.cutDirection.value = CutDirection.fromString(it)!!
                    }
                )

                DropDownSetting(
                    modifier = Modifier.width(400.dp),
                    settingName = "Strategy",
                    items = CuttingStrategy.values().map { it.text },
                    dropDownWidth = 220.dp,
                    selected = odTurningDataState.cuttingStrategy.value.text, onValueChanged = {
                        odTurningDataState.cuttingStrategy.value = CuttingStrategy.fromText(it)!!
                    })
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputSetting(
                    modifier = Modifier.width(400.dp),
                    inputType = InputType.TOOL_CLEARANCE, value = odTurningDataState.toolClearance.value.toString()
                ) {
                    odTurningDataState.toolClearance.value = it.toDouble()
                }
                InputSetting(
                    modifier = Modifier.width(400.dp),
                    inputType = InputType.CSS_MAX_RPM, value = odTurningDataState.spindleMaxSpeed.value.toString()
                ) {
                    odTurningDataState.spindleMaxSpeed.value = it.toDouble().toInt()
                }
            }
        }
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val cuttingParamsModifier = Modifier.width(400.dp)

            if (odTurningDataState.cuttingStrategy.value.isRoughing) {
                CuttingParametersView(
                    state = odTurningDataState.roughingParameters!!,
                    cuttingStrategy = CuttingStrategy.Roughing,
                    modifier = cuttingParamsModifier
                )
            }
            if (odTurningDataState.cuttingStrategy.value == CuttingStrategy.RoughingAndFinishing) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {})
                    {
                        Text("Fill same values")
                        Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {})
                    {
                        Text("Fill half values")
                        Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "")
                    }
                }
            }
            if (odTurningDataState.cuttingStrategy.value.isFinishing) {
                CuttingParametersView(
                    state = odTurningDataState.finishingParameters!!,
                    cuttingStrategy = CuttingStrategy.Finishing,
                    modifier = cuttingParamsModifier
                )
            }
        }
    }
}

@Composable
fun InputSetting(
    value: String,
    inputType: InputType,
    alternativeLabel: String? = null,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    val alignment = Alignment.CenterVertically
    val params = NumericInputs.entries[inputType]!!

    Row(
        verticalAlignment = alignment,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = alignment,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = alternativeLabel ?: params.valueDescription
            )
        }
        NumericInputWithUnit(value, inputType, alignment, modifier = Modifier.width(170.dp)) {
            onValueChanged(it)
        }
    }
}

