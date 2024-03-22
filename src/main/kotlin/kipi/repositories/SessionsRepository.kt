package kipi.repositories

import kipi.dao.Sessions
import kipi.dao.Sessions.expiredAt
import kipi.dao.Sessions.id
import kipi.dao.Sessions.initAt
import kipi.dao.Sessions.refreshToken
import kipi.dao.Sessions.token
import kipi.dao.Sessions.userId
import kipi.dto.Session
import kipi.exceptions.SessionException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime.now
import java.util.*

class SessionsRepository {

    fun createSession(id: Long, sessionLiveTimeMin: Long, refreshTokenId: Long? = null) = transaction {
        val now = now()

        Sessions.insert {
            it[userId] = id
            it[token] = UUID.randomUUID().toString()
            it[expiredAt] = now.plusMinutes(sessionLiveTimeMin)
            it[initAt] = now
            it[refreshToken] = refreshTokenId
        }.resultedValues?.map { mapToSession(it) }?.firstOrNull() ?: throw SessionException("auth.session.not.created")
    }

    fun forceDeleteSession(id: Long, refreshTokenId: Long) = transaction {
        Sessions.deleteWhere {
            (Sessions.id eq id) or
                    (Sessions.refreshToken eq refreshTokenId) or
                    (Sessions.id eq refreshTokenId)
        }
    }

    fun deleteAllUserSessions(userId: Long) = transaction {
        Sessions.deleteWhere {
            Sessions.userId eq userId
        }
    }

    fun findSession(token: String) = transaction {
        Sessions.select {
            Sessions.token eq token
        }.map {
            mapToSession(it)
        }.firstOrNull()
    }

    private fun mapToSession(resultRow: ResultRow) = Session(
        id = resultRow[id],
        userId = resultRow[userId],
        token = resultRow[token],
        expiredAt = resultRow[expiredAt],
        initAt = resultRow[initAt],
        refreshToken = resultRow[refreshToken]
    )
}