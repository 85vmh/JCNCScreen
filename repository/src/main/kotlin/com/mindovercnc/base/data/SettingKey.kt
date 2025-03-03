package com.mindovercnc.base.data

interface SettingKey

enum class IntegerKey : SettingKey {
    SpindleMode,
    FeedMode,
    RpmValue,
    CssValue,
    MaxCssRpm,
    LastToolUsed,
}

enum class DoubleKey : SettingKey {
    OrientAngle,
    FeedPerRev,
    FeedPerMin,
    TaperAngle,
    VirtualLimitXMinus,
    VirtualLimitXPlus,
    VirtualLimitZMinus,
    VirtualLimitZPlus;
}

enum class BooleanKey : SettingKey {
    OrientedStop,
    VirtualLimitXMinusActive,
    VirtualLimitXPlusActive,
    VirtualLimitZMinusActive,
    VirtualLimitZPlusActive,
    LimitZPlusIsToolRelated
}