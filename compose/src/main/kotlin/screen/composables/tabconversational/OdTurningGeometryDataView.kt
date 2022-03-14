package screen.composables.tabconversational

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.uimodel.InputType
import usecase.ConversationalUseCase
import usecase.model.TeachInAxis

@Composable
fun OdTurningGeometryData() {

    val useCase: ConversationalUseCase by rememberInstance()
    val odTurningDataState = remember { useCase.getOdTurningDataState() }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InputSetting(
            inputType = InputType.INITIAL_X,
            value = odTurningDataState.xInitial.value.toString(),
            teachInAxis = TeachInAxis.X,
            onTeachInClicked = {}
        ) {
            odTurningDataState.xInitial.value = it.toDouble()
        }
        InputSetting(
            inputType = InputType.FINAL_X,
            value = odTurningDataState.xFinal.value.toString(),
            teachInAxis = TeachInAxis.X,
            onTeachInClicked = {}
        ) {
            odTurningDataState.xFinal.value = it.toDouble()
        }
        InputSetting(
            inputType = InputType.Z_START,
            value = odTurningDataState.zStart.value.toString(),
            teachInAxis = TeachInAxis.Z,
            onTeachInClicked = {}
        ) {
            odTurningDataState.zStart.value = it.toDouble()
        }
        InputSetting(
            inputType = InputType.Z_END,
            value = odTurningDataState.zEnd.value.toString(),
            teachInAxis = TeachInAxis.Z,
            onTeachInClicked = {}
        ) {
            odTurningDataState.zEnd.value = it.toDouble()
        }
        InputSetting(
            inputType = InputType.FILLET_RADIUS,
            value = odTurningDataState.fillet.value.toString(),
            onTeachInClicked = {}
        ) {
            odTurningDataState.fillet.value = it.toDouble()
        }
    }
}