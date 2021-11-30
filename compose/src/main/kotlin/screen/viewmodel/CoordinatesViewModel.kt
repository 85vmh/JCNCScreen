package screen.viewmodel

import com.mindovercnc.base.CncStatusRepository
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.LengthUnit
import com.mindovercnc.base.data.getDisplayablePosition
import com.mindovercnc.base.data.isDiameterMode
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import screen.uimodel.AxisPosition
import screen.uimodel.RootScreenUiModel

class CoordinatesViewModel(
    cncStatusRepository: CncStatusRepository
) {
    val uiModel = cncStatusRepository.cncStatusFlow()
        .map {
            RootScreenUiModel(
                xAxisPos = it.toAxisPosition(AxisPosition.Axis.X),
                zAxisPos = it.toAxisPosition(AxisPosition.Axis.Z),
                isDiameterMode = it.isDiameterMode
            )
        }.distinctUntilChanged()
}

private fun CncStatus.toAxisPosition(axis: AxisPosition.Axis): AxisPosition {
    val value = when (axis) {
        AxisPosition.Axis.X -> getDisplayablePosition().x
        AxisPosition.Axis.Z -> getDisplayablePosition().z
    }
    val units = when (taskStatus.programUnits) {
        LengthUnit.MM -> AxisPosition.Units.MM
        LengthUnit.IN -> AxisPosition.Units.IN
        LengthUnit.CM -> AxisPosition.Units.CM
    }
    return AxisPosition(axis, value, if (axis == AxisPosition.Axis.X) value else null, units)
}