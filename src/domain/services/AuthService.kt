package domain.services

import Config
import exceptions.SessionException
import domain.repositories.SessionsRepository
import domain.repositories.UsersRepository
import domain.dto.Session
import domain.dto.TokensBlock
import domain.dto.User
import exceptions.AuthException
import java.time.LocalDateTime.now

class AuthService(
    private val usersRepository: UsersRepository,
    private val sessionsRepository: SessionsRepository,
    private val notificationsService: NotificationsService,
    private val config: Config
) {
    suspend fun login(phoneNumber: String) {
        val user = usersRepository.findUserByPhoneNumber(phoneNumber)
        val userId = if (user == null) {
            usersRepository.createUser(User(phoneNumber = phoneNumber))
        } else user.id
        notificationsService.sendCode(userId!!, phoneNumber)
    }

    suspend fun loginConfirm(phoneNumber: String, otpCode: String): TokensBlock {
        val user = usersRepository.findUserByPhoneNumber(phoneNumber)
            ?: throw AuthException("auth.credentials.incorrect")

        notificationsService.codeConfirm(user.id!!, otpCode)

        return createSessions(user.id)
    }

    fun logout(token: String) {
        val session = sessionsRepository.findSession(token)

        sessionsRepository.forceDeleteSession(session!!.id)
    }

    fun deleteUser(userId: Long) {
        sessionsRepository.deleteAllUserSessions(userId)
        usersRepository.deleteByUserId(userId)
    }

    fun verify(token: String): Session {
        val session = sessionsRepository.findSession(token) ?: throw SessionException("auth.session.not.exist")

        if (session.expiredAt <= now()) throw SessionException("auth.session.expired")

        return session
    }

    private fun createSessions(userId: Long): TokensBlock {
        val accessSession =
            sessionsRepository.createSession(userId, config.sessionAccessLiveTimeMin)

        return TokensBlock(accessSession)
    }
}