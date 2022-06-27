package vtk

fun vtkActor.translateToPoint(point: Point3D){
    val transform = vtkTransform().apply {
        Translate(point.x, point.y, point.z)
    }
    SetUserTransform(transform)
    Modified()
}

fun vtkAxesActor.translateToPoint(point: Point3D){
    val transform = vtkTransform().apply {
        Translate(point.x, point.y, point.z)
    }
    SetUserTransform(transform)
    Modified()
}