package usecase.model

import androidx.compose.runtime.mutableStateOf

class FeedState(
    defaultFeedRateMode: FeedRateMode,
    defaultUnitsPerRevValue: Double,
    defaultUnitsPerMinValue: Double,
) {
    val feedRateMode = mutableStateOf(defaultFeedRateMode)
    val unitsPerRevValue = mutableStateOf(defaultUnitsPerRevValue)
    val unitsPerMinValue = mutableStateOf(defaultUnitsPerMinValue)
}