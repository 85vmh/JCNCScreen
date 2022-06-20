package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.linuxcnc.model.ActiveCodes
import com.mindovercnc.repository.CncStatusRepository
import com.mindovercnc.repository.IniFileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import screen.composables.editor.Editor
import screen.uimodel.PositionModel
import usecase.*
import usecase.model.ActiveCode
import vtk.MachineLimits
import vtk.Point
import java.io.File

class ProgramLoadedScreenModel(
    private val file: File,
    private val gCodeUseCase: GCodeUseCase,
    offsetsUseCase: OffsetsUseCase,
    positionUseCase: PositionUseCase,
    private val activeCodesUseCase: ActiveCodesUseCase,
    private val programsUseCase: ProgramsUseCase,
    iniFileRepository: IniFileRepository
) : StateScreenModel<ProgramLoadedScreenModel.State>(State(editor = Editor(file))) {

    data class State(
        val positionModel: PositionModel? = null,
        val currentWcs: String = "--",
        val currentFolder: File? = null,
        val editor: Editor,
        val vtkUiState: VtkUiState = VtkUiState(),
        val activeCodes: List<ActiveCode> = emptyList(),
        val machineStatus: MachineStatus = MachineStatus()
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
                        ),
                        currentWcs = wcs.coordinateSystem
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

        activeCodesUseCase.getActiveCodes()
            .onEach { codes ->
                mutableState.update {
                    it.copy(
                        activeCodes = codes
                    )
                }
            }
            .launchIn(coroutineScope)

        programsUseCase.uiModel
            .onEach { model ->
                mutableState.update {
                    it.copy(
                        positionModel = model
                    )
                }
            }.launchIn(coroutineScope)
    }

    fun onActiveCodeClicked(activeCode: ActiveCode) {
        activeCodesUseCase.getCodeDescription(activeCode)
    }
}