package kipi.controllers

import kipi.services.AuthService

class DeleteUserController(
    private val authService: AuthService
) {
    fun handle(userId: Long) = authService.deleteUser(userId)
}