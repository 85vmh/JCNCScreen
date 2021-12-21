package usecase

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.Position
import com.mindovercnc.base.data.getDisplayablePosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import screen.uimodel.AxisPosition
import screen.uimodel.ManualPositionUiModel

class ManualPositionUseCase(
    private val scope: CoroutineScope,
    private val cncStatusRepository: CncStatusRepository
) {
    private data class PositionInfo(val position: Position, val xRelative: Boolean, val zRelative: Boolean)

    private var xZeroPos = 0.0
    private var zZeroPos = 0.0
    private val xRelativeState = MutableStateFlow(false)
    private val zRelativeState = MutableStateFlow(false)

    val uiModel = combine(
        getDisplayablePosition(),
        xRelativeState,
        zRelativeState
    ) { displayablePos, xRelative, zRelative -> PositionInfo(displayablePos, xRelative, zRelative) }
        .distinctUntilChanged()
        .map {
            val xAxisPosition = when (it.xRelative) {
                true -> AxisPosition(AxisPosition.Axis.X, it.position.x - xZeroPos, it.position.x, AxisPosition.Units.MM)
                false -> AxisPosition(AxisPosition.Axis.X, it.position.x, null, AxisPosition.Units.MM)
            }
            val zAxisPosition = when (it.zRelative) {
                true -> AxisPosition(AxisPosition.Axis.Z, it.position.z - zZeroPos, it.position.z, AxisPosition.Units.MM)
                false -> AxisPosition(AxisPosition.Axis.Z, it.position.z, null, AxisPosition.Units.MM)
            }
            ManualPositionUiModel(xAxisPosition, zAxisPosition, true)
        }


    fun setZeroPosX() {
        scope.launch {
            xZeroPos = getDisplayablePosition().map { it.x }.first()
            xRelativeState.value = true
        }
    }

    fun toggleXAbsRel() {
        xRelativeState.value = xRelativeState.value.not()
    }

    fun setZeroPosZ() {
        scope.launch {
            zZeroPos = getDisplayablePosition().map { it.z }.first()
            zRelativeState.value = true
        }
    }

    fun toggleZAbsRel() {
        zRelativeState.value = zRelativeState.value.not()
    }

    private fun getDisplayablePosition(): Flow<Position> {
        return cncStatusRepository.cncStatusFlow()
            .map { it.getDisplayablePosition() }
            .distinctUntilChanged()
    }
}