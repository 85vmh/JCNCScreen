package usecase.model

enum class FeedRateMode(code: Int) {
    INVERSE_TIME(93),
    UNITS_PER_MINUTE(94),
    UNITS_PER_REVOLUTION(95)
}