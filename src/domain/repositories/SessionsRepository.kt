package domain.repositories

import domain.dao.Sessions
import domain.dao.Sessions.expiredAt
import domain.dao.Sessions.id
import domain.dao.Sessions.initAt
import domain.dao.Sessions.refreshToken
import domain.dao.Sessions.token
import domain.dao.Sessions.userId
import domain.dto.Session
import exceptions.SessionException
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
                    (refreshToken eq refreshTokenId) or
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