package controllers

import controllers.response.SessionResponse
import domain.services.AuthService

class VerifyController(
    private val authService: AuthService
) {
    fun handle(token: String): SessionResponse {
        val session = authService.verify(token)

        return SessionResponse(session.userId, accessToken = session.token)
    }
}