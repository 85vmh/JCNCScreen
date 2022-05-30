package vtk

const val length = 2.5 //taken from py

class PathActor(
    pathElements: List<PathElement>,
    multiplicationFactor: Double = 1.0
) : vtkActor() {

    private val axesActor = AxesActor()

    private data class PathColor(
        val alpha: Int,
        val red: Int,
        val green: Int,
        val blue: Int
    )
    private val lineColors = vtkUnsignedCharArray().apply {
        SetNumberOfComponents(4)
    }
    private val arcColors = vtkUnsignedCharArray().apply {
        SetNumberOfComponents(4)
    }

    private fun vtkUnsignedCharArray.addPathColor(color: PathColor) {
        // it seems that vtk expects the color in format of rgba
        InsertNextTuple4(color.red.toDouble(), color.green.toDouble(), color.blue.toDouble(), color.alpha.toDouble())
    }

    private val feedColor = PathColor(255, 255, 255, 255)
    private val rapidColor = PathColor(100, 255, 255, 255)

    init {
        axesActor.SetTotalLength(length, 0.0, length)
        val multiPolyData = vtkAppendPolyData()

        val linesPolyData = vtkPolyData()
        val lines = vtkCellArray()
        val points = vtkPoints()

        pathElements.forEach {
            println("----$it")
            when (it) {
                is Line -> {
                    val startId = points.InsertNextPoint(it.startPoint.toDoubleArray(multiplicationFactor))
                    val endId = points.InsertNextPoint(it.endPoint.toDoubleArray(multiplicationFactor))
                    when (it.type) {
                        Line.Type.Traverse -> lineColors.addPathColor(rapidColor)
                        Line.Type.Feed -> lineColors.addPathColor(feedColor)
                    }
                    val line = vtkLine().apply {
                        GetPointIds().SetId(0, startId)
                        GetPointIds().SetId(1, endId)
                    }
                    lines.InsertNextCell(line)
                    Modified()
                }
                is Arc -> {
                    arcColors.addPathColor(feedColor)
                    val arc = vtkArcSource().apply {
                        SetPoint1(it.startPoint.toDoubleArray(multiplicationFactor))
                        SetPoint2(it.endPoint.toDoubleArray(multiplicationFactor))
                        SetCenter(it.centerPoint.toDoubleArray(multiplicationFactor))
                        SetResolution(30)
                        Update()
                    }
                    val arcPolyData = arc.GetOutput().apply {
                        GetCellData().SetScalars(arcColors)
                    }
                    multiPolyData.AddInputData(arcPolyData)
                }
            }
        }

        linesPolyData.apply {
            GetCellData().SetScalars(lineColors)
            SetPoints(points)
            SetLines(lines)
            Modified()
        }
        multiPolyData.AddInputData(linesPolyData)

        val dataMapper = vtkPolyDataMapper().apply {
            SetInputConnection(multiPolyData.GetOutputPort())
            SetColorModeToDirectScalars()
            SetScalarModeToUseCellData()
            Update()
        }
        SetMapper(dataMapper)
    }
}