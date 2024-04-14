package kipi.controllers

import kipi.dto.Credentials
import kipi.dto.Notification
import kipi.dto.SessionResponse
import kipi.services.AuthService
import kipi.services.NotificationService
import kipi.utils.ConfirmationUtils.generateCode

class RegistrationController(
    private val authService: AuthService,
    private val notificationService: NotificationService
) {
    fun handle(userInfo: Credentials): SessionResponse {
        val sessions = authService.registration(userInfo)
        val code = generateCode()
        notificationService.sendNotification(
            Notification(
                beneficiary = userInfo.email!!,
                title = "Подтверждение почты",
                message = "Введите в приложении код ${generateCode()} для подтверждения адреса почты"
            )
        )

        authService.updateConfirmCode(sessions.refreshSession.userId, code)

        return SessionResponse(
            sessions.refreshSession.userId,
            sessions.refreshSession.token,
            sessions.accessSession.token
        )
    }
}