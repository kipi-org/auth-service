package domain.services

import domain.clients.NotificationsServiceClient
import domain.clients.request.NotificationConfirmRequest
import domain.clients.request.NotificationRequest

class NotificationsService(
    private val notificationsServiceClient: NotificationsServiceClient,
) {
    suspend fun sendCode(userId: Long, phoneNumber: String) {
        notificationsServiceClient.sendCode(userId, NotificationRequest(phoneNumber))
    }

    suspend fun codeConfirm(userId: Long, otpCode: String) {
        notificationsServiceClient.codeConfirm(userId, NotificationConfirmRequest(otpCode))
    }
}