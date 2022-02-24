package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.AxisLimits
import com.mindovercnc.base.data.BooleanKey
import com.mindovercnc.base.data.DoubleKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import usecase.model.VirtualLimitsState

class VirtualLimitsUseCase(
    private val scope: CoroutineScope,
    private val statusRepository: CncStatusRepository,
    private val commandRepository: CncCommandRepository,
    private val halRepository: HalRepository,
    private val settingsRepository: SettingsRepository,
    private val iniFileRepository: IniFileRepository
) {

    private val _isLimitsActive = MutableStateFlow(false)

    val isLimitsActive = _isLimitsActive.asStateFlow()

    init {
        applyLimits()
    }

    fun toggleLimitsActive() {
        _isLimitsActive.value = _isLimitsActive.value.not()
        if (_isLimitsActive.value) {
            setCustomLimits(getVirtualLimitsState())
        }
        iniFileRepository.toggleCustomLimits()
        applyLimits()
    }

    fun getVirtualLimitsState() = VirtualLimitsState(
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
        setCustomLimits(limits)
        applyLimits()
    }

    private fun setCustomLimits(limits: VirtualLimitsState) {
        val axisLimits = AxisLimits(
            xMinLimit = if (limits.xMinusActive.value) limits.xMinus.value else null,
            xMaxLimit = if (limits.xPlusActive.value) limits.xPlus.value else null,
            zMinLimit = if (limits.zMinusActive.value) limits.zMinus.value else null,
            zMaxLimit = if (limits.zPlusActive.value) limits.zPlus.value else null
        )
        iniFileRepository.setCustomAxisLimits(axisLimits)
    }

    private fun applyLimits() {
        with(iniFileRepository.getActiveLimits()) {
            halRepository.setAxisLimitXMin(this.xMinLimit!!)
            halRepository.setAxisLimitXMax(this.xMaxLimit!!)
            halRepository.setAxisLimitZMin(this.zMinLimit!!)
            halRepository.setAxisLimitZMax(this.zMaxLimit!!)
            println("---HAL Apply: $this")
        }
    }
}