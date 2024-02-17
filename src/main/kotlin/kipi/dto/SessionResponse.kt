package kipi.dto

import java.io.Serializable

data class SessionResponse(
    val userId: Long,
    val refreshToken: String? = null,
    val accessToken: String,
) : Serializable
