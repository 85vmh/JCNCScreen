package usecase

import codegen.Point
import com.mindovercnc.base.*
import com.mindovercnc.base.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import usecase.model.VirtualLimitsState

class VirtualLimitsUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository,
    private val iniFileRepository: IniFileRepository
) {

    var isInEditMode = false
    private val _isLimitsActive = MutableStateFlow(false)

    val isLimitsActive = _isLimitsActive.asStateFlow()

    init {
        statusRepository.cncStatusFlow()
            .map { it.currentToolNo }
            .filter { it > 0 }
            .distinctUntilChanged()
            .filter { _isLimitsActive.value }
            .filter { !isInEditMode } //react on tool changes only when not in edit mode
            .onEach {
                println("---Apply for tool: $it")
                val storedLimits = VirtualLimitsState(
                    xMinus = settingsRepository.get(DoubleKey.VirtualLimitXMinus, Double.MIN_VALUE),
                    xPlus = settingsRepository.get(DoubleKey.VirtualLimitXPlus, Double.MAX_VALUE),
                    zMinus = settingsRepository.get(DoubleKey.VirtualLimitZMinus, Double.MIN_VALUE),
                    zPlus = settingsRepository.get(DoubleKey.VirtualLimitZPlus, Double.MAX_VALUE),
                    xMinusActive = settingsRepository.get(BooleanKey.VirtualLimitXMinusActive),
                    xPlusActive = settingsRepository.get(BooleanKey.VirtualLimitXPlusActive),
                    zMinusActive = settingsRepository.get(BooleanKey.VirtualLimitZMinusActive),
                    zPlusActive = settingsRepository.get(BooleanKey.VirtualLimitZPlusActive),
                )
                setCustomLimits(storedLimits)
                applyActiveLimits()
            }
            .launchIn(scope)

        applyActiveLimits()
    }

    fun toggleLimitsActive() {
        _isLimitsActive.value = _isLimitsActive.value.not()
        scope.launch {
            if (_isLimitsActive.value) {
                setCustomLimits(virtualLimitsState)
            }
            iniFileRepository.toggleCustomLimits()
            applyActiveLimits()
        }
    }

    val virtualLimitsState = VirtualLimitsState(
        xMinus = settingsRepository.get(DoubleKey.VirtualLimitXMinus, Double.MIN_VALUE),
        xPlus = settingsRepository.get(DoubleKey.VirtualLimitXPlus, Double.MAX_VALUE),
        zMinus = settingsRepository.get(DoubleKey.VirtualLimitZMinus, Double.MIN_VALUE),
        zPlus = settingsRepository.get(DoubleKey.VirtualLimitZPlus, Double.MAX_VALUE),
        xMinusActive = settingsRepository.get(BooleanKey.VirtualLimitXMinusActive),
        xPlusActive = settingsRepository.get(BooleanKey.VirtualLimitXPlusActive),
        zMinusActive = settingsRepository.get(BooleanKey.VirtualLimitZMinusActive),
        zPlusActive = settingsRepository.get(BooleanKey.VirtualLimitZPlusActive),
    )

    fun saveVirtualLimits(limits: VirtualLimitsState) {
        settingsRepository.apply {
            put(DoubleKey.VirtualLimitXMinus, limits.xMinus.value)
            put(DoubleKey.VirtualLimitXPlus, limits.xPlus.value)
            put(DoubleKey.VirtualLimitZMinus, limits.zMinus.value)
            put(DoubleKey.VirtualLimitZPlus, limits.zPlus.value)
            put(BooleanKey.VirtualLimitXMinusActive, limits.xMinusActive.value)
            put(BooleanKey.VirtualLimitXPlusActive, limits.xPlusActive.value)
            put(BooleanKey.VirtualLimitZMinusActive, limits.zMinusActive.value)
            put(BooleanKey.VirtualLimitZPlusActive, limits.zPlusActive.value)
        }
        scope.launch {
            setCustomLimits(limits)
            applyActiveLimits()
        }
    }

    private suspend fun setCustomLimits(limits: VirtualLimitsState) {
        //z display position = machinePosition.z - g5xOffset.z - toolOffset.z
        //z display position + g5xOffset.z + toolOffset.z = machinePosition.z

        //am un tool position, sa zicem Z=-30 -> fatza bacurilor
        //teach in -> ia absolute position pt T1 si il seteaza ca virtual limit.
        //         -> stocheaza Z-30 ca si valoare de referinta pentru sistemul de coordonate curent
        //load T2, (-30 + g5xOffset + toolOffset) -> noul absolute position pt T2 -> seteaza new virtual limit

        val relativeToolPosition = statusRepository.cncStatusFlow()
            .map { it.getRelativeToolPosition() }
            .map { Point(it.x * 2, it.z) } // *2 due to diameter mode
            .first()

        val g53XMinus = relativeToolPosition.x + limits.xMinus.value
        val g53XPlus = relativeToolPosition.x + limits.xPlus.value
        val g53ZMinus = relativeToolPosition.z + limits.zMinus.value
        val g53ZPlus = relativeToolPosition.z + limits.zPlus.value

        val g53AxisLimits = G53AxisLimits(
            xMinLimit = if (limits.xMinusActive.value) g53XMinus / 2 else null,
            xMaxLimit = if (limits.xPlusActive.value) g53XPlus / 2 else null,
            zMinLimit = if (limits.zMinusActive.value) g53ZMinus else null,
            zMaxLimit = if (limits.zPlusActive.value) g53ZPlus else null
        )
        iniFileRepository.setCustomG53AxisLimits(g53AxisLimits)
    }

    private fun applyActiveLimits() {
        with(iniFileRepository.getActiveLimits()) {
            halRepository.setAxisLimitXMin(this.xMinLimit!!)
            halRepository.setAxisLimitXMax(this.xMaxLimit!!)
            halRepository.setAxisLimitZMin(this.zMinLimit!!)
            halRepository.setAxisLimitZMax(this.zMaxLimit!!)
            println("---HAL Apply: $this")
        }
    }

    private suspend fun getCurrentPoint() = statusRepository.cncStatusFlow()
        .map { it.getDisplayablePosition() }
        .map { Point(it.x * 2, it.z) } // *2 due to diameter mode
        .first()

    fun teachInXMinus() {
        scope.launch {
            virtualLimitsState.xMinus.value = getCurrentPoint().x
        }
    }

    fun teachInXPlus() {
        scope.launch {
            virtualLimitsState.xPlus.value = getCurrentPoint().x
        }
    }

    fun teachInZMinus() {
        scope.launch {
            virtualLimitsState.zMinus.value = getCurrentPoint().z
        }
    }

    fun teachInZPlus() {
        scope.launch {
            virtualLimitsState.zPlus.value = getCurrentPoint().z
        }
    }
}