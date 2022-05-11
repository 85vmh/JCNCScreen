package usecase

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import ui.screen.manual.root.HandWheelsUiModel

class HandWheelsUseCase(
    statusRepository: CncStatusRepository,
    halRepository: HalRepository,
) {

    @OptIn(FlowPreview::class)
    val handWheelsUiModel = combine(
        statusRepository.cncStatusFlow().map { it.isInManualMode },
        halRepository.jogIncrementValue().debounce(200L)
    ) { isManualMode, jogIncrement -> HandWheelsUiModel(isManualMode, jogIncrement) }
}