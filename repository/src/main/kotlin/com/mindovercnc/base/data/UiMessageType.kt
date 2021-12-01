package com.mindovercnc.base.data

enum class UiMessageType {
    MachineInEstop,
    MachineNotON,
    MachineNotHomed,
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