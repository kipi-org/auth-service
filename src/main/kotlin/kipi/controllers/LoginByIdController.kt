package kipi.controllers

import kipi.dto.IdCredentials
import kipi.dto.SessionResponse
import kipi.services.AuthService

class LoginByIdController(
    private val authService: AuthService
) {
    fun handle(userInfo: IdCredentials): SessionResponse {
        val sessions = authService.login(userInfo)

        return SessionResponse(
            sessions.refreshSession.userId,
            sessions.refreshSession.token,
            sessions.accessSession.token
        )
    }
}