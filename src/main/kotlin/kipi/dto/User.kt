package kipi.dto

import java.io.Serializable

data class User(
    val id: Long? = null,
    val username: String,
    val hashedPassword: String,
    val sessions: List<Session> = emptyList()
) : Serializable
