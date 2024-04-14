package kipi.dto

import java.io.Serializable

data class Credentials(
    val username: String,
    val password: String
) : Serializable