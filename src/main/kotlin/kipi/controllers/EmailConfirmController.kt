package kipi.controllers

import kipi.dto.EmailConfirmRequest
import kipi.services.AuthService

class EmailConfirmController(
    private val authService: AuthService
) {
    fun handle(emailConfirmRequest: EmailConfirmRequest) =
        authService.emailConfirm(emailConfirmRequest.userId, emailConfirmRequest.code)
}