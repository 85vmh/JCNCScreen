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
    TaperAngle;
}

enum class BooleanKey() : SettingKey {
    OrientedStop
}