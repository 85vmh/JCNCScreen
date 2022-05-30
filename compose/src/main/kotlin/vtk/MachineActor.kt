package vtk

data class MachineLimits(
    val xMin: Double,
    val xMax: Double,
    val yMin: Double,
    val yMax: Double,
    val zMin: Double,
    val zMax: Double
)

class MachineActor(machineLimits: MachineLimits) : vtkCubeAxesActor() {
    val units = "mm"

    init {
        with(machineLimits){
            SetBounds(xMin, xMax, yMin, yMax, zMin, zMax)
        }
        SetXLabelFormat("%6.3f")
        SetYLabelFormat("%6.3f")
        SetZLabelFormat("%6.3f")
        SetFlyModeToStaticEdges()
        GetTitleTextProperty(0).SetColor(1.0, 0.0, 0.0)
        GetLabelTextProperty(0).SetColor(1.0, 0.0, 0.0)
        GetTitleTextProperty(1).SetColor(0.0, 1.0, 0.0)
        GetLabelTextProperty(1).SetColor(0.0, 1.0, 0.0)
        GetTitleTextProperty(2).SetColor(0.0, 0.0, 1.0)
        GetLabelTextProperty(2).SetColor(0.0, 0.0, 1.0)
        SetXUnits(units)
        SetYUnits(units)
        SetZUnits(units)
        DrawXGridlinesOn()
        DrawYGridlinesOn()
        DrawZGridlinesOn()
        //SetGridLineLocation(VTK_GRID_LINES_FURTHEST)
        GetXAxesGridlinesProperty().SetColor(0.0, 0.0, 0.0)
        GetYAxesGridlinesProperty().SetColor(0.0, 0.0, 0.0)
        GetZAxesGridlinesProperty().SetColor(0.0, 0.0, 0.0)
    }

    fun showMachineBounds(show: Boolean) {
        if (show) {
            XAxisVisibilityOn()
            YAxisVisibilityOn()
            ZAxisVisibilityOn()
        } else {
            XAxisVisibilityOff()
            YAxisVisibilityOff()
            ZAxisVisibilityOff()
        }
    }

    fun showMachineLabels(show: Boolean) {
        if (show) {
            XAxisLabelVisibilityOn()
            YAxisLabelVisibilityOn()
            ZAxisLabelVisibilityOn()
        } else {
            XAxisLabelVisibilityOff()
            YAxisLabelVisibilityOff()
            ZAxisLabelVisibilityOff()
        }
    }

    fun showGridLines(show: Boolean) {
        if (show) {
            DrawXGridlinesOn()
            DrawYGridlinesOn()
            DrawZGridlinesOn()
        } else {
            DrawZGridlinesOff()
            DrawZGridlinesOff()
            DrawZGridlinesOff()
        }
    }
}