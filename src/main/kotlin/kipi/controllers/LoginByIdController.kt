package kipi.controllers

import kipi.dto.IdCredentials
import kipi.dto.SessionResponse
import kipi.services.AuthService

class LoginByIdController(
    private val authService: AuthService
) {
    fun handle(userInfo: IdCredentials): SessionResponse {
        val session = authService.login(userInfo)

        return SessionResponse(session.userId, session.token)
    }
}