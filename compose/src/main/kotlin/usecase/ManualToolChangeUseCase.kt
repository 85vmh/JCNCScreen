package usecase

import com.mindovercnc.repository.CncStatusRepository
import com.mindovercnc.repository.HalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class ManualToolChangeUseCase(
    scope: CoroutineScope,
    statusRepository: CncStatusRepository,
    private val halRepository: HalRepository,
) {
    private val _toolNo = MutableStateFlow<Int?>(null)

    init {
        combine(
            halRepository.getToolChangeRequest().distinctUntilChanged(),
            statusRepository.cncStatusFlow().map { it.isInAutoMode }.distinctUntilChanged(),
            halRepository.getToolChangeToolNumber().distinctUntilChanged(),
        ) { _, isAuto, toolNo ->
            when {
                isAuto -> requestManualToolChange(toolNo)
                else -> confirmToolChange()
            }
        }.launchIn(scope)
    }

    val toolToChange = _toolNo.asSharedFlow().distinctUntilChanged()

    private fun requestManualToolChange(toolNo: Int) {
        println("**requestManualToolChange: $toolNo")
        _toolNo.value = toolNo
    }

    suspend fun confirmToolChange() {
        println("**confirmToolChange")
        halRepository.setToolChangedResponse()
        _toolNo.value = null
    }

    fun cancelToolChange() {
        println("**cancelToolChange")
        _toolNo.value = null
    }
}