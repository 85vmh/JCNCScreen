package com.mindovercnc.base.data

enum class UiMessageType {
    MachineInEstop,
    MachineNotON,
    MachineNotHomed,
    SpindleOperationNotAllowed,
    SpindleForwardNotAllowed,
    SpindleReverseNotAllowed,
    JoystickOperationNotAllowed,
    ReachedMinLimitX,
    ReachedMaxLimitX,
    ReachedMinLimitZ,
    ReachedMaxLimitZ
}