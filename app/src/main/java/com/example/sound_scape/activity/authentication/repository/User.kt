import java.io.Serializable

data class User(
    val userId: String? = null,
    val email: String? = null,
    val username: String? = null
) : Serializable