package com.mindovercnc.base.data

interface SettingKey

enum class IntegerKey() : SettingKey {
    SpindleMode,
    FeedMode,
    RpmValue,
    CssValue,
    MaxCssRpm,
}

enum class DoubleKey() : SettingKey {
    OrientAngle,
    FeedPerRev,
    FeedPerMin,
    TaperAngle,
    VirtualLimitXMinus,
    VirtualLimitXPlus,
    VirtualLimitZMinus,
    VirtualLimitZPlus;
}

enum class BooleanKey() : SettingKey {
    OrientedStop,
    VirtualLimitsActive,
    VirtualLimitXMinusActive,
    VirtualLimitXPlusActive,
    VirtualLimitZMinusActive,
    VirtualLimitZPlusActive
}