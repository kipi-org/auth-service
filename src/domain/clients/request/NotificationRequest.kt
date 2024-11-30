package domain.clients.request

import java.io.Serializable

data class NotificationRequest(
    val receiverPhoneNumber: String,
): Serializable
