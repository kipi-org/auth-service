import controllers.*
import domain.clients.NotificationsServiceClient
import domain.repositories.SessionsRepository
import domain.repositories.UsersRepository
import domain.services.AuthService
import domain.services.NotificationsService
import utils.HttpClientGeneratorUtils.generateHttpClient

class Dependencies {
    val config = Config()

    private val usersRepository = UsersRepository()
    private val sessionsRepository = SessionsRepository()

    private val notificationsServiceClient =
        NotificationsServiceClient(generateHttpClient(config.notificationsServiceUrl))

    private val notificationsService = NotificationsService(notificationsServiceClient)
    private val authService = AuthService(usersRepository, sessionsRepository, notificationsService, config)

    val loginController = LoginController(authService)
    val logoutController = LogoutController(authService)
    val deleteUserController = DeleteUserController(authService)
    val verifyController = VerifyController(authService)
    val revokeController = RevokeController(authService)
}