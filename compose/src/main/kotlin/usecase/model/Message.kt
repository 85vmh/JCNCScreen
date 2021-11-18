package usecase.model

data class Message(
    val text: String,
    val level: Level = Level.INFO,
    val persistence: Persistence = Persistence.TRANSIENT
) {
    enum class Level {
        INFO, WARNING, ERROR, DEBUG
    }

    enum class Persistence {
        TRANSIENT, PERSISTENT
    }
}