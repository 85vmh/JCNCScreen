package vtk

class AxesActor(axisMask: Int = 3) : vtkAxesActor() {
    init {
        val length = 20.0 //hardcoded from py

        val transform = vtkTransform()
        transform.Translate(0.0, 0.0, 0.0)

        SetUserTransform(transform)
        AxisLabelsOff()
        SetShaftTypeToLine()
        SetTipTypeToCone()

        when (axisMask) {
            3 -> SetTotalLength(length, length, 0.0)
            5 -> SetTotalLength(length, 0.0, length)
            6 -> SetTotalLength(0.0, length, length)
        }
    }
}