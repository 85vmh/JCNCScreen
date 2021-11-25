package screen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mindovercnc.base.CncCommandRepository
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import screen.BaseScreen

class BaseScreenViewModel constructor(
    scope: CoroutineScope,
    private val cncStatusRepository: CncStatusRepository,
    private val cncCommandRepository: CncCommandRepository,
    startupScreen: BaseScreen
) {
    var screen by mutableStateOf(startupScreen)

    init {
        cncStatusRepository.cncStatusFlow()
            .map { it.machineHomed() }
            .distinctUntilChanged()
            .onEach {
                screen = if (it) {
                    BaseScreen.RootScreen
                } else {
                    BaseScreen.NotHomedScreen
                }
            }
            .launchIn(scope)
    }

    fun turningSettingsClicked() {
        screen = BaseScreen.TurningSettingsScreen
    }

    fun turningSettingsClose() {
        screen = BaseScreen.RootScreen
    }
}

fun CncStatus.machineHomed(): Boolean {
    motionStatus.jointsStatus.forEach {
        if (it.isHomed.not()) return false
    }
    return true
}