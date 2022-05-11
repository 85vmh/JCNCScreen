package usecase

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.SettingsRepository
import com.mindovercnc.base.data.BooleanKey
import com.mindovercnc.base.data.DoubleKey
import com.mindovercnc.base.data.IntegerKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import ui.screen.manual.root.SpindleUiModel

class SpindleUseCase(
    private val statusRepository: CncStatusRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository
) {

    fun spindleFlow(): Flow<SpindleUiModel> {
        return combine(
            settingsRepository.flow(BooleanKey.IsRpmMode),
            settingsRepository.flow(IntegerKey.RpmValue),
            settingsRepository.flow(IntegerKey.CssValue),
            settingsRepository.flow(IntegerKey.MaxCssRpm),
            spindleOverride(),
            actualSpindleSpeed(),
            stopAngleFlow(),
        ) { values ->
            SpindleUiModel(
                isRpmMode = values[0] as Boolean,
                setRpm = values[1] as Int,
                setCss = values[2] as Int,
                maxRpm = values[3] as Int,
                spindleOverride = values[4] as Int,
                actualRpm = values[5] as Int,
                stopAngle = values[6] as Double?,
            )
        }
    }

    private fun spindleOverride() = statusRepository.cncStatusFlow()
        .map { it.motionStatus.spindlesStatus[0].spindleScale }
        .map { it.toInt() }
        .distinctUntilChanged()

    private fun actualSpindleSpeed() = halRepository.actualSpindleSpeed()
        .map { it.toInt() }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun stopAngleFlow(): Flow<Double?> {
        return settingsRepository.flow(BooleanKey.OrientedStop)
            .flatMapLatest {
                when {
                    it -> settingsRepository.flow(DoubleKey.OrientAngle)
                    else -> flowOf(null)
                }
            }
    }
}