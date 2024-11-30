package controllers

import controllers.request.LoginRequest
import controllers.request.OtpConfirmationRequest
import controllers.response.SessionResponse
import domain.services.AuthService

class LoginController(
    private val authService: AuthService
) {
    suspend fun login(loginRequest: LoginRequest) {
        authService.login(loginRequest.phoneNumber)
    }

    suspend fun loginConfirm(otpConfirmationRequest: OtpConfirmationRequest): SessionResponse {
        val sessions = authService.loginConfirm(otpConfirmationRequest.phoneNumber, otpConfirmationRequest.otpCode)

        return SessionResponse(
            sessions.refreshSession.userId,
            sessions.refreshSession.token,
            sessions.accessSession.token
        )
    }
}