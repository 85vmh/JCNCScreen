package com.mindovercnc.base.data

data class JoystickStatus(
    val position: Position,
    val isRapid: Boolean = false
) {

    enum class Position {
        Neutral, XPlus, XMinus, ZPlus, ZMinus
    }
}