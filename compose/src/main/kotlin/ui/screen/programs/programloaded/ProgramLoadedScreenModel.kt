package ui.screen.programs.programloaded

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import canvas.Point2D
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
import usecase.model.*
import vtk.Point3D
import java.io.File
import kotlin.math.min

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

    //how much free space to have around the drawing
    private val viewportPadding = 70 //px
    private val toolTrace = mutableListOf<Point2D>()

    init {
//        programsUseCase.loadProgram(file)
        val machineLimits = with(iniFileRepository.getActiveLimits()) {
            MachineLimits(
                xMin = xMinLimit!!,
                xMax = xMaxLimit!!,
                zMin = zMinLimit!!,
                zMax = zMaxLimit!!
            )
        }
        mutableState.update {
            it.copy(
                vtkUiState = it.vtkUiState.copy(
                    machineLimits = machineLimits
                ),
                visualTurningState = it.visualTurningState.copy(
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
                        currentWcs = wcs.coordinateSystem,
                        visualTurningState = it.visualTurningState.copy(
                            currentWcs = wcs.coordinateSystem,
                            wcsPosition = Point2D(wcs.xOffset, wcs.zOffset)
                        )
                    )
                }
            }
            .launchIn(coroutineScope)

        coroutineScope.launch {
            val pathElements = gCodeUseCase.getPathElements(file)
            val initialProgramData = pathElements.toProgramData()
            val defaultPixelsPerUnit = calculateDefaultPxPerUnit(
                viewportSize = mutableState.value.visualTurningState.viewportSize,
                programSize = initialProgramData.programSize,
            )
            val scaledProgramData = pathElements.toProgramData(defaultPixelsPerUnit)
            mutableState.update {
                it.copy(
                    vtkUiState = it.vtkUiState.copy(
                        pathElements = pathElements,
                    ),
                    visualTurningState = it.visualTurningState.copy(
                        pathElements = pathElements,
                        programData = scaledProgramData,
                        defaultPixelsPerUnit = defaultPixelsPerUnit,
                        translate = scaledProgramData.getInitialTranslate(
                            viewportSize = it.visualTurningState.viewportSize
                        )
                    )
                )
            }
        }

        positionUseCase.getToolPosition()
            .onEach { point ->
                toolTrace.add(point)
                mutableState.update {
                    it.copy(
                        vtkUiState = it.vtkUiState.copy(
                            toolPosition = Point3D(point.x, 0.0, point.z)
                        ),
                        visualTurningState = it.visualTurningState.copy(
                            toolPosition = point,
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

    fun zoomOut() {
        setNewScale(mutableState.value.visualTurningState.scale / 1.1f)
    }

    fun zoomIn() {
        setNewScale(mutableState.value.visualTurningState.scale * 1.1f)
    }

    private fun setNewScale(newScale: Float) {
        mutableState.update {
            val pixelPerUnit = it.visualTurningState.defaultPixelsPerUnit * newScale
            val scaledProgramData = it.visualTurningState.pathElements.toProgramData(pixelPerUnit)
            it.copy(
                visualTurningState = it.visualTurningState.copy(
                    scale = newScale,
                    programData = scaledProgramData,
                    translate = scaledProgramData.getInitialTranslate(
                        viewportSize = it.visualTurningState.viewportSize
                    )
                )
            )
        }
    }

    fun translate(offset: Offset) {
        mutableState.update {
            it.copy(
                visualTurningState = it.visualTurningState.copy(
                    translate = it.visualTurningState.translate.plus(offset)
                )
            )
        }
        println("Translate: ${mutableState.value.visualTurningState.translate}")
    }

    fun setViewportSize(size: IntSize) {
        mutableState.update {
            it.copy(
                visualTurningState = it.visualTurningState.copy(
                    viewportSize = size
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

    private fun calculateDefaultPxPerUnit(
        viewportSize: IntSize,
        programSize: ProgramData.ProgramSize,
    ): Float {
        val drawableWidth = viewportSize.width - viewportPadding
        val drawableHeight = viewportSize.height - viewportPadding
        val widthRatio = drawableWidth.div(programSize.width)
        val heightRatio = drawableHeight.div(programSize.height)
        return min(widthRatio, heightRatio)
    }

    private fun List<PathElement>.toProgramData(pixelPerUnit: Float = 1f): ProgramData {
        val fp = Path()
        val tp = Path()
        forEach {
            when (it) {
                is PathElement.Line -> {
                    when (it.type) {
                        PathElement.Line.Type.Feed -> fp.addLine(it, pixelPerUnit)
                        PathElement.Line.Type.Traverse -> tp.addLine(it, pixelPerUnit)
                    }
                }
                is PathElement.Arc -> fp.addArc(it, pixelPerUnit)
            }
        }
        fp.close()
        tp.close()

        return ProgramData(
            feedPath = fp,
            traversePath = tp
        )
    }

    private fun List<Point2D>.toPath(pixelPerUnit: Float): Path {
        val path = Path()
        val previousPoint = firstOrNull()
        if (previousPoint != null) {
            with(previousPoint.toOffset(pixelPerUnit)) {
                path.moveTo(x, y)
            }
            this.forEach {
                with(it.toOffset(pixelPerUnit)) {
                    path.lineTo(x, y)
                }
            }
        }
        return path
    }
}