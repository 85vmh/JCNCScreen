package vtk

class ProgramBoundsActor(camera: vtkCamera, pathActor: PathActor) : vtkCubeAxesActor() {
    init {
        SetBounds(pathActor.GetBounds())
        SetCamera(camera)

        SetXLabelFormat("%6.3f") //not sure about this format
        SetYLabelFormat("%6.3f")
        SetZLabelFormat("%6.3f")

        SetFlyModeToStaticEdges()

        GetTitleTextProperty(0).SetColor(1.0, 0.0, 0.0)
        GetLabelTextProperty(0).SetColor(1.0, 0.0, 0.0)

        GetTitleTextProperty(1).SetColor(0.0, 1.0, 0.0)
        GetLabelTextProperty(1).SetColor(0.0, 1.0, 0.0)

        GetTitleTextProperty(2).SetColor(0.0, 0.0, 1.0)
        GetLabelTextProperty(2).SetColor(0.0, 0.0, 1.0)
    }

    fun showProgramLabels(show: Boolean) {
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

    fun showProgramTicks(show: Boolean) {
        if (show) {
            XAxisTickVisibilityOn()
            XAxisTickVisibilityOn()
            XAxisTickVisibilityOn()
        } else {
            XAxisTickVisibilityOff()
            XAxisTickVisibilityOff()
            XAxisTickVisibilityOff()
        }
    }

    fun showProgramBounds(show: Boolean) {
        if (show) {
            XAxisVisibilityOn()
            YAxisVisibilityOn()
            ZAxisVisibilityOn()
        } else {
            XAxisVisibilityOff()
            XAxisVisibilityOff()
            XAxisVisibilityOff()
        }
    }

}
