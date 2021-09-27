package com.mindovercnc.dummycnc

import com.mindovercnc.base.data.Position
import com.mindovercnc.base.data.PositionState

object PositionMock {
    fun mock(x : Double = 1.0) =  PositionState(
        absPos = Position(x = x, y = 2.0, z = 3.0),
        g5xPos = Position(x = 1.0, y = 2.0, z = 3.0),
        toolPos = Position(x = 1.0, y = 2.0, z = 3.0),
        g92Pos = Position(x = 1.0, y = 2.0, z = 3.0),
    )
}