package controllers.response

data class SessionResponse(
    val userId: Long,
    val accessToken: String,
)
