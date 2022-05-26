package vtk

class Line3DActor(val start: Point, val end: Point) : vtkActor() {
    init {

        val source = vtkLineSource()
        //source.SetNumberOfSides(1)
//        source.SetRadius(0.035)
//        source.SetCenter(0.0, 0.0, 0.0)

        val points = vtkPoints()
        points.InsertNextPoint(start.toDoubleArray())
        points.InsertNextPoint(end.toDoubleArray())
        val lines = vtkCellArray()
        lines.InsertNextCell(2)
        lines.InsertCellPoint(0)
        lines.InsertCellPoint(1)
        val polyData = vtkPolyData().apply {
            SetPoints(points)
            SetLines(lines)
        }
        val mapper = vtkPolyDataMapper().apply {
            SetInputData(polyData)
            //SetInputConnection(source.GetOutputPort())
            Update()
        }
        SetMapper(mapper)

    }
}