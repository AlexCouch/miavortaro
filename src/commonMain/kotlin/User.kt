@kotlinx.serialization.Serializable
data class User(
    val username: String,
    val passwordHashed: String
)