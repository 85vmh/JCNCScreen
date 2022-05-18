package usecase

import com.mindovercnc.base.SettingsRepository
import com.mindovercnc.linuxcnc.model.DoubleKey
import kotlinx.coroutines.flow.Flow

class TaperUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun taperAngleFlow(): Flow<Double> {
        return settingsRepository.flow(DoubleKey.TaperAngle)
    }
}