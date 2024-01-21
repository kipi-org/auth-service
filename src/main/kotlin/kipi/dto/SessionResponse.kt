package kipi.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val userId: Long,
    val token: String
)
