package com.mindovercnc.base.data

data class JoystickStatus(
    val position: Position,
    val isRapid: Boolean = false
) {

    enum class Position {
        NEUTRAL, X_PLUS, X_MINUS, Z_PLUS, Z_MINUS
    }
}