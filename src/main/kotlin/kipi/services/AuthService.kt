package kipi.services

import kipi.Config
import kipi.dto.Credentials
import kipi.dto.IdCredentials
import kipi.dto.Session
import kipi.dto.User
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
    fun registration(userInfo: Credentials) = with(userInfo) {
        validate(userInfo)

        if (usersRepository.findUserByUsername(username) != null) throw AuthException("auth.username.exist")

        val userId = usersRepository.createUser(
            User(
                username = username,
                hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            )
        )
        return@with sessionsRepository.createSession(userId, config.sessionLiveTimeMin)
    }

    fun login(userInfo: Credentials) = with(userInfo) {
        val user = usersRepository.findUserByUsername(username) ?: throw AuthException("auth.credentials.incorrect")

        if (BCrypt.checkpw(password, user.hashedPassword)) return@with sessionsRepository.createSession(
            user.id!!,
            config.sessionLiveTimeMin
        )
        else throw AuthException("auth.credentials.incorrect")
    }

    fun login(userInfo: IdCredentials) = with(userInfo) {
        val user = usersRepository.findUserById(id) ?: throw AuthException("auth.credentials.incorrect")

        if (BCrypt.checkpw(password, user.hashedPassword)) return@with sessionsRepository.createSession(
            user.id!!,
            config.sessionLiveTimeMin
        )
        else throw AuthException("auth.credentials.incorrect")
    }

    fun logout(token: String) = sessionsRepository.deleteSession(token)

    fun verify(token: String): Session {
        val session = sessionsRepository.findSession(token) ?: throw SessionException("auth.session.not.exist")

        if (session.expiredAt <= now()) throw SessionException("auth.session.expired")

        return session
    }

    fun revoke(token: String): Session {
        val oldSession = sessionsRepository.findSession(token) ?: throw SessionException("auth.session.not.exist")
        sessionsRepository.deleteSession(token)

        return sessionsRepository.createSession(oldSession.userId, config.sessionLiveTimeMin)
    }
}