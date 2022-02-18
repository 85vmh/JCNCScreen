package screen.composables.tabmanual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.toFixedDigits
import screen.composables.CardWithTitle
import screen.uimodel.InputType
import usecase.ManualTurningUseCase
import usecase.model.TaperState

@Composable
fun TaperSettingsView(
    viewModel: TaperSettingsViewModel,
    modifier: Modifier
) {

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardWithTitle("Known Angle") {
            ValueSetting(
                settingName = "Taper Angle",
                value = viewModel.taperState.taperAngle.value.toFixedDigits(),
                inputType = InputType.TAPER_ANGLE
            ) {
                val doubleValue = it.toDoubleOrNull() ?: return@ValueSetting
                viewModel.taperState.taperAngle.value = doubleValue
            }
        }
        CardWithTitle("Unknown Angle") {

        }
    }
}

class TaperSettingsViewModel(
    val useCase: ManualTurningUseCase
) {
    val taperState: TaperState = useCase.getTaperState()

    fun save() {
        useCase.applyTaperSettings(taperState)
    }
}
