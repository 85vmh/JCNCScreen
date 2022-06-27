package vtk

class AxesActor : vtkAxesActor() {

    var currentPoint: Point3D = Point3D(0.0, 0.0, 0.0)
        set(value) {
            if (field != value) {
                field = value
                translateToPoint(value)
            }
        }

    init {
        val length = 20.0 //hardcoded from py

        val transform = vtkTransform()
        transform.Translate(0.0, 0.0, 0.0)

        SetUserTransform(transform)
        AxisLabelsOn()
        GetXAxisShaftProperty().apply {
            SetColor(0.0, 255.0, 0.0)
//            SetOpacity(0.5)
            SetLineWidth(2.0)
        }
        GetXAxisTipProperty().apply {
            SetColor(0.0, 255.0, 0.0)
        }
        GetXAxisCaptionActor2D().GetTextActor().GetTextProperty().apply {
            BoldOff()
            ItalicOff()
        }
        GetZAxisCaptionActor2D().GetTextActor().GetTextProperty().apply {
            BoldOff()
            ItalicOff()
        }
        SetYAxisLabelText("")
        SetShaftTypeToLine()
        SetTotalLength(length, 0.0, length)
    }
}