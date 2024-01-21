package kipi.controllers

import kipi.dto.SessionResponse
import kipi.services.AuthService

class VerifyController(
    private val authService: AuthService
) {
    fun handle(token: String): SessionResponse {
        val session = authService.verify(token)

        return SessionResponse(session.userId, session.token)
    }
}