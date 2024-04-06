package kipi.controllers

import kipi.dto.RecoverConfirmRequest
import kipi.services.AuthService

class RecoverConfirmController(
    private val authService: AuthService
) {
    fun handle(recoverConfirmRequest: RecoverConfirmRequest) {
        authService.confirmRecover(recoverConfirmRequest)
    }
}