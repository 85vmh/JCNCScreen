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
    FeedPerMin;
}

enum class BooleanKey() : SettingKey {
    OrientedStop
}