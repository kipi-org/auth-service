package kipi.services

import kipi.Config
import kipi.dto.*
import kipi.exceptions.AuthException
import kipi.exceptions.SessionException
import kipi.repositories.SessionsRepository
import kipi.repositories.UsersRepository
import kipi.validators.UserCredentialsValidator.validate
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime.now

class AuthService(
    private val usersRepository: UsersRepository,
    private val sessionsRepository: SessionsRepository,
    private val config: Config
) {
    fun registration(userInfo: Credentials): TokensBlock = with(userInfo) {
        validate(userInfo)

        if (usersRepository.findUserByUsername(username) != null) throw AuthException("auth.username.exist")

        val userId = usersRepository.createUser(
            User(
                username = username,
                hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            )
        )

        createSessions(userId)
    }

    fun login(userInfo: Credentials): TokensBlock = with(userInfo) {
        val user = usersRepository.findUserByUsername(username) ?: throw AuthException("auth.credentials.incorrect")

        if (BCrypt.checkpw(password, user.hashedPassword)) {
            createSessions(user.id!!)
        } else throw AuthException("auth.credentials.incorrect")
    }

    fun login(userInfo: IdCredentials): TokensBlock = with(userInfo) {
        val user = usersRepository.findUserById(id) ?: throw AuthException("auth.credentials.incorrect")

        if (BCrypt.checkpw(password, user.hashedPassword)) {
            createSessions(user.id!!)
        } else throw AuthException("auth.credentials.incorrect")
    }

    fun logout(token: String) {
        val session = sessionsRepository.findSession(token)

        when {
            session == null -> return
            session.refreshToken == null -> return
            else -> sessionsRepository.forceDeleteSession(session.id, session.refreshToken)
        }
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