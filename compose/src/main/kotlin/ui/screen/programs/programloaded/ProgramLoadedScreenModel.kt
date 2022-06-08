package ui.screen.programs.programloaded

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mindovercnc.base.IniFileRepository
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import screen.composables.editor.Editor
import usecase.GCodeUseCase
import usecase.OffsetsUseCase
import usecase.PositionUseCase
import vtk.*
import java.io.File

class ProgramLoadedScreenModel(
    private val file: File,
    private val gCodeUseCase: GCodeUseCase,
    private val offsetsUseCase: OffsetsUseCase,
    private val positionUseCase: PositionUseCase,
    iniFileRepository: IniFileRepository
) : StateScreenModel<ProgramLoadedScreenModel.State>(State()) {

    data class State(
        val currentFolder: File? = null,
        val editor: Editor? = null,
        val vtkState: VtkState? = null,
    )

    private val camera = vtkCamera().apply {
        ParallelProjectionOn()
        OrthogonalizeViewUp()
    }
    private val panel = vtkPanel()
    private val renderWindow = panel.GetRenderWindow()
    private val renderer = panel.GetRenderer().apply {
        SetActiveCamera(camera)
    }

    private var machineActor: MachineActor
    private val axesActor = AxesActor()
    private val toolActor = ToolActor()

    lateinit var programBoundsActor: ProgramBoundsActor
    lateinit var pathActor: PathActor

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
        machineActor = MachineActor(machineLimits)

        mutableState.update {
            it.copy(
                vtkState = VtkState(
                    vtkPanel = panel,
                    vtkCamera = camera,
                    vtkRenderWindow = renderWindow,
                    vtkRenderer = renderer,
                    actors = listOf(toolActor, machineActor),
                    axesActors = listOf(axesActor)
                )
            )
        }

        offsetsUseCase.currentOffset
            .filterNotNull()
            .onEach { wcs ->
                if (::pathActor.isInitialized) {
                    val transform = vtkTransform().apply {
                        Translate(wcs.xOffset, 0.0, wcs.zOffset)
                    }
                    pathActor.SetUserTransform(transform)
                    axesActor.SetUserTransform(transform)

                    renderer.RemoveActor(programBoundsActor)
                    programBoundsActor = ProgramBoundsActor(pathActor)
                    programBoundsActor.SetCamera(camera)
                    renderer.AddActor(programBoundsActor)

                    renderer.Render()
                    renderWindow.Render()
                }
            }
            .launchIn(coroutineScope)

        coroutineScope.launch {
            pathActor = gCodeUseCase.getPathActor(file)
            programBoundsActor = ProgramBoundsActor(pathActor)

            mutableState.update {
                it.copy(
                    vtkState = VtkState(
                        vtkPanel = panel,
                        vtkCamera = camera,
                        vtkRenderWindow = renderWindow,
                        vtkRenderer = renderer,
                        actors = listOf(toolActor, machineActor, pathActor, programBoundsActor),
                        axesActors = listOf(axesActor)
                    )
                )
            }
        }


    }

    fun setView1() {
        setCameraView(camera, 90.0, x = 1.0)
        //camera.SetViewUp(1.0, 0.0, 0.0)
        renderWindow.Render()
    }

    fun updatePosition(){
        positionUseCase.getToolActorPosition()
            .onEach {
                val transform = vtkTransform().apply {
                    Translate(it.x, 0.0, it.z)
                }
                toolActor.SetUserTransform(transform)
                renderer.Render()
                renderWindow.Render()
            }
            .launchIn(coroutineScope)
    }

    fun setCameraView(camera: vtkCamera, angle: Double, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) {
        val fp = camera.GetFocalPoint()
        val transform = vtkTransform().apply {
//        Translate(fp[0], fp[1], fp[2])
            RotateWXYZ(-90.0, 1.0, 0.0, 0.0)
            RotateY(90.0)
//        Translate(-fp[0], -fp[1], -fp[2])
        }
        camera.ApplyTransform(transform)
    }
}