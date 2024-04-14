package kipi

import kipi.controllers.*
import kipi.repositories.SessionsRepository
import kipi.repositories.UsersRepository
import kipi.services.AuthService
import kipi.services.NotificationService

class Dependencies {
    val config = Config()

    private val usersRepository = UsersRepository()
    private val sessionsRepository = SessionsRepository()

    private val authService = AuthService(usersRepository, sessionsRepository, config)
    private val notificationService = NotificationService(config)

    val registrationController = RegistrationController(authService)
    val loginController = LoginController(authService)
    val loginByIdController = LoginByIdController(authService)
    val logoutController = LogoutController(authService)
    val deleteUserController = DeleteUserController(authService)
    val verifyController = VerifyController(authService)
    val revokeController = RevokeController(authService)
    val recoverController = RecoverController(notificationService, authService)
    val recoverConfirmController = RecoverConfirmController(authService)
    val emailController = EmailController(authService, notificationService)
    val emailConfirmController = EmailConfirmController(authService)
}