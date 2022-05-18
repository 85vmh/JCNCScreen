import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.linuxcnc.model.isEstop
import com.mindovercnc.linuxcnc.model.isHomed
import com.mindovercnc.linuxcnc.model.isOn
import kotlinx.coroutines.flow.*

data class AppState(
    val isBottomBarEnabled: Boolean = true,
)

class TabViewModel(
    cncStatusRepository: CncStatusRepository
) : StateScreenModel<AppState>(AppState()) {

    init {
        cncStatusRepository.cncStatusFlow()
            .map { it.isEstop.not() && it.isOn && it.isHomed() }
            .distinctUntilChanged()
            .onEach { ready ->
                mutableState.update {
                    it.copy(isBottomBarEnabled = ready)
                }
            }
            .launchIn(coroutineScope)
    }
}