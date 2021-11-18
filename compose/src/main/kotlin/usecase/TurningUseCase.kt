package usecase

import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class TurningUseCase(
    val statusRepository: CncStatusRepository,
    val commandRepository: CncCommandRepository,
    val halRepository: HalRepository
) {

    val spindleMode = statusRepository.cncStatusFlow()
        .map { it.taskStatus.activeCodes.spindleMode }
        .distinctUntilChanged()

    val feedMode = statusRepository.cncStatusFlow()
        .map { it.taskStatus.activeCodes.feedMode }
        .distinctUntilChanged()

    val distanceMode = statusRepository.cncStatusFlow()
        .map { it.taskStatus.activeCodes.distanceMode }
        .distinctUntilChanged()

    val setFeedRate = statusRepository.cncStatusFlow()
        .map { it.taskStatus.setFeedRate }
        .distinctUntilChanged()

    val feedOverride = statusRepository.cncStatusFlow()
        .map { it.motionStatus.trajectoryStatus.velocityScale }

    val actualFeedRate = combine(setFeedRate, feedOverride) { feed, scale -> (feed ?: 0.0) * scale / 100 }
        .distinctUntilChanged()

    val setSpindleSpeed = statusRepository.cncStatusFlow()
        .map { it.taskStatus.setSpindleSpeed }
        .distinctUntilChanged()

    val spindleOverride = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].spindleScale }
        .distinctUntilChanged()

    val actualSpindleSpeed = halRepository.actualSpindleSpeed().distinctUntilChanged()

    val cssMaxSpeed = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].cssMaximum }
        .distinctUntilChanged()
}