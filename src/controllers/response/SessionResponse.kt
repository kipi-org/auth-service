package controllers.response

data class SessionResponse(
    val userId: Long,
    val refreshToken: String? = null,
    val accessToken: String,
)
