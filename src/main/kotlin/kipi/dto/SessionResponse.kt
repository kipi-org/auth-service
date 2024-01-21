package kipi.dto

import java.io.Serializable

data class SessionResponse(
    val userId: Long,
    val token: String
) : Serializable
