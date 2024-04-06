package kipi.services

import kipi.Config
import kipi.dto.Notification
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class NotificationService(
    private val config: Config
) {
    fun sendNotification(notification: Notification) {
        val email = SimpleEmail()
        email.hostName = "smtp.gmail.com"
        email.setSmtpPort(587)
        email.setAuthenticator(DefaultAuthenticator(config.mailUser, config.mailToken))
        email.isSSLOnConnect = true
        email.setFrom(config.mailUser)
        email.subject = notification.title
        email.setMsg(notification.message)
        email.addTo(notification.beneficiary)
        email.send()
    }
}