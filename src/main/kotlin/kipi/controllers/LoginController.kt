package kipi.controllers

import kipi.dto.Credentials
import kipi.dto.SessionResponse
import kipi.services.AuthService

class LoginController(
    private val authService: AuthService
) {
    fun handle(userInfo: Credentials): SessionResponse {
        val sessions = authService.login(userInfo)

        return SessionResponse(
            sessions.refreshSession.userId,
            sessions.refreshSession.token,
            sessions.accessSession.token
        )
    }
}