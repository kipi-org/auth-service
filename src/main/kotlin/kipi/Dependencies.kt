package kipi

import kipi.controllers.*
import kipi.repositories.SessionsRepository
import kipi.repositories.UsersRepository
import kipi.services.AuthService

class Dependencies {
    private val usersRepository = UsersRepository()
    private val sessionsRepository = SessionsRepository()
    private val authService = AuthService(usersRepository, sessionsRepository)

    val registrationController = RegistrationController(authService)
    val loginController = LoginController(authService)
    val logoutController = LogoutController(authService)
    val verifyController = VerifyController(authService)
    val revokeController = RevokeController(authService)
}