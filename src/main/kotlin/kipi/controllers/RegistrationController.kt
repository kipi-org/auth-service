package kipi.controllers

import kipi.dto.Credentials
import kipi.dto.SessionResponse
import kipi.services.AuthService

class RegistrationController(
    private val authService: AuthService
) {
    fun handle(userInfo: Credentials): SessionResponse {
        val session = authService.registration(userInfo)

        return SessionResponse(session.userId, session.token)
    }
}