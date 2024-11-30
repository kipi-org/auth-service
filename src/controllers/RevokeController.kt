package controllers

import controllers.response.SessionResponse
import domain.services.AuthService

class RevokeController(
    private val authService: AuthService
) {
    fun handle(token: String): SessionResponse {
        val session = authService.revoke(token)

        return SessionResponse(session.userId, accessToken = session.token)
    }
}