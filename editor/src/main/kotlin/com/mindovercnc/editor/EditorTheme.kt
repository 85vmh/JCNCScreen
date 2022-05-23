package com.mindovercnc.editor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditorTheme constructor(
    @SerialName("background")
    val background: IntColor,
    @SerialName("lineNumber")
    val lineNumber: IntColor,
    @SerialName("comment")
    val comment: IntColor,
    @SerialName("value")
    val value: IntColor,
    @SerialName("text")
    val text: IntColor,
    @SerialName("keyword")
    val keyword: IntColor,
    @SerialName("punctuation")
    val punctuation: IntColor
)

@JvmInline
@Serializable
value class IntColor(val value: Long)