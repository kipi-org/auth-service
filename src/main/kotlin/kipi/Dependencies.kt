package kipi

import kipi.controllers.*
import kipi.repositories.SessionsRepository
import kipi.repositories.UsersRepository
import kipi.services.AuthService

class Dependencies {
    val config = Config()

    private val usersRepository = UsersRepository()
    private val sessionsRepository = SessionsRepository()
    private val authService = AuthService(usersRepository, sessionsRepository, config)

    val registrationController = RegistrationController(authService)
    val loginController = LoginController(authService)
    val loginByIdController = LoginByIdController(authService)
    val logoutController = LogoutController(authService)
    val verifyController = VerifyController(authService)
    val revokeController = RevokeController(authService)
}