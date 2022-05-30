package vtk

interface PathElement

data class Line(
    val startPoint: Point,
    val endPoint: Point,
    val type: Type
) : PathElement {
    enum class Type {
        Traverse, Feed
    }
}

data class Arc(
    val startPoint: Point,
    val endPoint: Point,
    val centerPoint: Point,
    val direction: Direction
) : PathElement{
    enum class Direction{
        Clockwise,
        CounterClockwise
    }
}