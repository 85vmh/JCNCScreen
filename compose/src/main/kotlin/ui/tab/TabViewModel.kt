import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.repository.CncStatusRepository
import com.mindovercnc.model.isEstop
import com.mindovercnc.model.isHomed
import com.mindovercnc.model.isOn
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