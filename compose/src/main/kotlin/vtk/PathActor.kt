package vtk

const val length = 2.5 //taken from py

class PathActor : vtkActor() {
    private val axesActor = AxesActor()
    val points = vtkPoints()
    val lines = vtkCellArray()
    val polyData = vtkPolyData()
    val dataMapper = vtkPolyDataMapper()


    init {
        axesActor.SetTotalLength(length, 0.0, length)
        vtkUnsignedCharArray().SetNumberOfComponents(4)
    }

}