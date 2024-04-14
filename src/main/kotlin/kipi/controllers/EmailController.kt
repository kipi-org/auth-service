package kipi.controllers

import kipi.dto.EmailRequest
import kipi.dto.Notification
import kipi.exceptions.AuthException
import kipi.services.AuthService
import kipi.services.NotificationService
import kipi.utils.ConfirmationUtils.generateCode

class EmailController(
    private val authService: AuthService,
    private val notificationService: NotificationService
) {
    fun handle(emailRequest: EmailRequest) {
        authService.findUser(emailRequest.userId) ?: throw AuthException("auth.credentials.incorrect")

        val code = generateCode()
        notificationService.sendNotification(
            Notification(
                beneficiary = emailRequest.email,
                title = "Подтверждение почты",
                message = "Введите в приложении код ${generateCode()} для подтверждения адреса почты"
            )
        )

        authService.updateConfirmCode(emailRequest.userId, code)
    }
}