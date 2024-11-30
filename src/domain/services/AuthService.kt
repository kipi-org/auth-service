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

        when {
            session == null -> return
            session.refreshToken == null -> return
            else -> sessionsRepository.forceDeleteSession(session.id, session.refreshToken)
        }
    }

    fun deleteUser(userId: Long) {
        sessionsRepository.deleteAllUserSessions(userId)
        usersRepository.deleteByUserId(userId)
    }

    fun verify(token: String): Session {
        val session = sessionsRepository.findSession(token) ?: throw SessionException("auth.session.not.exist")

        if (session.refreshToken == null) throw SessionException("auth.session.illegal.token")
        if (session.expiredAt <= now()) throw SessionException("auth.session.expired")

        return session
    }

    fun revoke(refreshToken: String): Session {
        val refreshSession =
            sessionsRepository.findSession(refreshToken) ?: throw SessionException("auth.session.not.exist")

        if (refreshSession.refreshToken != null) throw SessionException("auth.session.illegal.token")
        if (refreshSession.expiredAt <= now()) throw SessionException("auth.session.expired")

        return sessionsRepository.createSession(
            refreshSession.userId,
            config.sessionAccessLiveTimeMin,
            refreshSession.id
        )
    }

    private fun createSessions(userId: Long): TokensBlock {
        val refreshSession = sessionsRepository.createSession(userId, config.sessionRefreshLiveTimeMin)
        val accessSession =
            sessionsRepository.createSession(userId, config.sessionRefreshLiveTimeMin, refreshSession.id)

        return TokensBlock(accessSession, refreshSession)
    }
}