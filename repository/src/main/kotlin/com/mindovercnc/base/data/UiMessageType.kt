package com.mindovercnc.base.data

enum class UiMessageType(val isError: Boolean = false) {
    MachineInEstop(true),
    MachineNotON(false),
    SpindleOperationNotAllowed,
    SpindleForwardNotAllowed,
    SpindleReverseNotAllowed,
    JoystickCannotFeedWithSpindleOff,
    JoystickResetRequired,
    ReachedMinSoftLimitX,
    ReachedMaxSoftLimitX,
    ReachedMinSoftLimitZ,
    ReachedMaxSoftLimitZ
}