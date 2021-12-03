package usecase.model

data class HandwheelsState(
    val active: Boolean,
    val increment: Float,
    val unit: String = "mm"
)