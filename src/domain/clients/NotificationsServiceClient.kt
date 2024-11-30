package domain.clients

import domain.clients.request.NotificationConfirmRequest
import domain.clients.request.NotificationRequest
import exceptions.ServiceException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class NotificationsServiceClient(
    private val client: HttpClient
) {
    suspend fun sendCode(userId: Long, notificationRequest: NotificationRequest) {
        val response = client.post {
            url { path("/users/$userId/send/confirmation") }
            contentType(ContentType.Application.Json)
            setBody(notificationRequest)
        }
        if (response.status != HttpStatusCode.OK) {
            throw ServiceException(response.bodyAsText())
        }
    }

    suspend fun codeConfirm(userId: Long, notificationConfirmRequest: NotificationConfirmRequest) {
        val response = client.put {
            url { path("/users/$userId/send/confirmation") }
            contentType(ContentType.Application.Json)
            setBody(notificationConfirmRequest)
        }

        if (response.status != HttpStatusCode.OK) {
            throw ServiceException(response.bodyAsText())
        }
    }
}