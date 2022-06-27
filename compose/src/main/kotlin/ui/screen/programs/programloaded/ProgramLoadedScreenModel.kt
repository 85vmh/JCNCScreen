package ui.screen.programs.programloaded

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import canvas.addArc
import canvas.addLine
import com.mindovercnc.repository.IniFileRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import screen.composables.editor.Editor
import screen.uimodel.PositionModel
import usecase.*
import usecase.model.ActiveCode
import usecase.model.VisualTurningState
import vtk.MachineLimits
import vtk.PathElement
import vtk.Point3D
import java.io.File

class ProgramLoadedScreenModel(
    private val file: File,
    private val gCodeUseCase: GCodeUseCase,
    offsetsUseCase: OffsetsUseCase,
    positionUseCase: PositionUseCase,
    private val activeCodesUseCase: ActiveCodesUseCase,
    private val programsUseCase: ProgramsUseCase,
    spindleUseCase: SpindleUseCase,
    feedUseCase: FeedUseCase,
    iniFileRepository: IniFileRepository
) : StateScreenModel<ProgramLoadedScreenModel.State>(State(editor = Editor(file))) {

    data class State(
        val positionModel: PositionModel? = null,
        val currentWcs: String = "--",
        val currentFolder: File? = null,
        val editor: Editor,
        val vtkUiState: VtkUiState = VtkUiState(),
        val visualTurningState: VisualTurningState = VisualTurningState(),
        val activeCodes: List<ActiveCode> = emptyList(),
        val machineStatus: MachineStatus = MachineStatus(),
        val toolChangeModel: ToolChangeModel? = null,
        val useVtk: Boolean = false
    )

    init {
        programsUseCase.loadProgram(file)
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
                            wcsPosition = Point3D(wcs.xOffset, 0.0, wcs.zOffset)
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
                        pathElements = pathElements,
                    ),
                    visualTurningState = pathElements.toProgramPaths()
                )
            }
        }

        positionUseCase.getToolActorPosition()
            .onEach { point ->
                mutableState.update {
                    it.copy(
                        vtkUiState = it.vtkUiState.copy(
                            toolPosition = Point3D(point.x, 0.0, point.z)
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

        spindleUseCase.spindleFlow()
            .onEach { model ->
                mutableState.update {
                    it.copy(
                        machineStatus = it.machineStatus.copy(
                            spindleOverride = model.spindleOverride,
                            actualSpindleSpeed = model.actualRpm
                        )
                    )
                }
            }.launchIn(coroutineScope)

        feedUseCase.feedFlow()
            .onEach { model ->
                mutableState.update {
                    it.copy(
                        machineStatus = it.machineStatus.copy(
                            feedOverride = model.feedOverride.toInt(),
                        )
                    )
                }
            }.launchIn(coroutineScope)
    }

    private fun List<PathElement>.toProgramPaths(): VisualTurningState {
        val scale = 12f

        val feedPath = Path()
        val traversePath = Path()

        forEach {
            when (it) {
                is PathElement.Line -> {
                    when (it.type) {
                        PathElement.Line.Type.Feed -> feedPath.addLine(it, scale)
                        PathElement.Line.Type.Traverse -> traversePath.addLine(it, scale)
                    }
                }
                is PathElement.Arc -> feedPath.addArc(it, scale)
            }
        }
        feedPath.close()
        traversePath.close()

        return VisualTurningState(
            feedPath = feedPath,
            traversePath = traversePath
        )
    }

    fun zoomOut(){
        mutableState.update {
            it.copy(
                visualTurningState = it.visualTurningState.copy(
                    scale = it.visualTurningState.scale / 1.1f
                )
            )
        }
    }

    fun zoomIn(){
        mutableState.update {
            it.copy(
                visualTurningState = it.visualTurningState.copy(
                    scale = it.visualTurningState.scale * 1.1f
                )
            )
        }
    }

    fun translate(offset: Offset){
        mutableState.update {
            it.copy(
                visualTurningState = it.visualTurningState.copy(
                    translate = it.visualTurningState.translate.plus(offset)
                )
            )
        }
    }

    fun runProgram() {
//        programsUseCase.runProgram()
        mutableState.update {
            it.copy(
                toolChangeModel = ToolChangeModel(10)
            )
        }
    }

    fun stopProgram() {
        programsUseCase.stopProgram()
    }

    fun confirmToolChanged() {
        mutableState.update {
            it.copy(
                toolChangeModel = null
            )
        }
    }

    fun cancelToolChange() {
        mutableState.update {
            it.copy(
                toolChangeModel = null
            )
        }
    }

    fun setVtkState(useVtk: Boolean) {
        mutableState.update {
            it.copy(
                useVtk = useVtk
            )
        }
    }

    fun onActiveCodeClicked(activeCode: ActiveCode) {
        activeCodesUseCase.getCodeDescription(activeCode)
    }
}