package kipi.repositories

import kipi.dao.Sessions
import kipi.dao.Sessions.expiredAt
import kipi.dao.Sessions.id
import kipi.dao.Sessions.initAt
import kipi.dao.Sessions.token
import kipi.dao.Sessions.userId
import kipi.dto.Session
import kipi.exceptions.SessionException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime.now
import java.util.*

class SessionsRepository {

    fun createSession(id: Long) = transaction {
        val now = now()

        Sessions.insert {
            it[userId] = id
            it[token] = UUID.randomUUID().toString()
            it[expiredAt] = now.plusMinutes(15)
            it[initAt] = now
        }.resultedValues?.map { mapToSession(it) }?.firstOrNull() ?: throw SessionException("Session not created")
    }

    fun deleteSession(token: String) = transaction {
        Sessions.deleteWhere {
            Sessions.token eq token
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
        initAt = resultRow[initAt]
    )
}