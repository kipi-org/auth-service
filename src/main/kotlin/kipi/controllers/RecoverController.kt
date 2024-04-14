package kipi.controllers

import kipi.dto.Notification
import kipi.dto.RecoverRequest
import kipi.services.AuthService
import kipi.services.NotificationService
import kipi.utils.ConfirmationUtils.generateCode

class RecoverController(
    private val notificationService: NotificationService,
    private val authService: AuthService
) {
    fun handle(recoverRequest: RecoverRequest) {
        val code = generateCode()
        authService.updateConfirmCode(recoverRequest.userId, code)
        notificationService.sendNotification(
            Notification(
                beneficiary = recoverRequest.email,
                title = "Восстановление пароля",
                message = "Введите код ${generateCode()} в приложении"
            )
        )
    }
}