package screen.viewmodel

import usecase.ManualTurningUseCase
import usecase.model.FeedState
import usecase.model.SpindleState

class TurningSettingsViewModel(
    val useCase: ManualTurningUseCase
) {
    val spindleState: SpindleState = useCase.getSpindleState()
    val feedState: FeedState = useCase.getFeedState()

    fun save() {
        useCase.applyFeedSettings(feedState)
        useCase.applySpindleSettings(spindleState)
    }
}