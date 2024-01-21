package kipi.controllers

import kipi.dto.SessionResponse
import kipi.services.AuthService

class RevokeController(
    private val authService: AuthService
) {
    fun handle(token: String): SessionResponse {
        val session = authService.revoke(token)

        return SessionResponse(session.userId, session.token)
    }
}