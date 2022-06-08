package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.base.IniFileRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import screen.composables.editor.Editor
import usecase.GCodeUseCase
import usecase.OffsetsUseCase
import usecase.PositionUseCase
import vtk.MachineLimits
import vtk.Point
import java.io.File

class ProgramLoadedScreenModel(
    private val file: File,
    private val gCodeUseCase: GCodeUseCase,
    offsetsUseCase: OffsetsUseCase,
    positionUseCase: PositionUseCase,
    iniFileRepository: IniFileRepository
) : StateScreenModel<ProgramLoadedScreenModel.State>(State()) {

    data class State(
        val currentFolder: File? = null,
        val editor: Editor? = null,
        val vtkUiState: VtkUiState = VtkUiState(),
    )

    init {
        val iniLimits = iniFileRepository.getActiveLimits()
        val machineLimits = MachineLimits(
            xMin = iniLimits.xMinLimit!!,
            xMax = iniLimits.xMaxLimit!!,
            yMin = 0.0,
            yMax = 0.0,
            zMin = iniLimits.zMinLimit!!,
            zMax = iniLimits.zMaxLimit!!
        )
        mutableState.update {
            it.copy(
                vtkUiState = it.vtkUiState.copy(
                    machineLimits = machineLimits
                )
            )
        }

        offsetsUseCase.currentOffset
            .filterNotNull()
            .onEach { wcs ->
                mutableState.update {
                    it.copy(
                        vtkUiState = it.vtkUiState.copy(
                            wcsPosition = Point(wcs.xOffset, 0.0, wcs.zOffset)
                        )
                    )
                }
            }
            .launchIn(coroutineScope)

        coroutineScope.launch {
            val pathElements = gCodeUseCase.getPathElements(file)
            mutableState.update {
                it.copy(
                    vtkUiState = it.vtkUiState.copy(
                        pathElements = pathElements
                    )
                )
            }
        }

        positionUseCase.getToolActorPosition()
            .onEach { point ->
                mutableState.update {
                    it.copy(
                        vtkUiState = it.vtkUiState.copy(
                            toolPosition = Point(point.x, 0.0, point.z)
                        )
                    )
                }
            }
            .launchIn(coroutineScope)
    }
}