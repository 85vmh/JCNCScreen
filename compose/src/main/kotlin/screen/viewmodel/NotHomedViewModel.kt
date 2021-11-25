package screen.viewmodel

import com.mindovercnc.base.CncStatusRepository
import kotlinx.coroutines.flow.map
import screen.uimodel.NotHomedUiModel

class NotHomedViewModel(
    cncStatusRepository: CncStatusRepository
) {
    val uiModel = cncStatusRepository.cncStatusFlow()
        .map {
            with(it.motionStatus) {
                NotHomedUiModel(
                    xHomed = this.jointsStatus[0].isHomed,
                    zHomed = this.jointsStatus[1].isHomed,
                    xHoming = this.jointsStatus[0].isHoming,
                    zHoming = this.jointsStatus[1].isHoming,
                )
            }
        }
}