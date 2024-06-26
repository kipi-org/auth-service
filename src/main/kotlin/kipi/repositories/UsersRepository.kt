package kipi.repositories

import kipi.dao.Users
import kipi.dao.Users.currentRecoverCode
import kipi.dao.Users.hashedPassword
import kipi.dao.Users.id
import kipi.dao.Users.isEmailConfirmed
import kipi.dao.Users.recoverCodeExpiredAt
import kipi.dao.Users.username
import kipi.dto.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime.now

class UsersRepository {

    fun createUser(user: User) = transaction {
        Users.insert {
            it[username] = user.username
            it[hashedPassword] = user.hashedPassword
            it[currentRecoverCode] = user.currentRecoverCode
            it[recoverCodeExpiredAt] = user.recoverCodeExpiredAt
            it[isEmailConfirmed] = user.isEmailConfirmed
        }[Users.id]
    }

    fun findUserByUsername(username: String) = transaction {
        Users.select {
            Users.username eq username
        }.map { mapToUser(it) }.firstOrNull()
    }

    fun findUserById(id: Long) = transaction {
        Users.select {
            Users.id eq id
        }.map { mapToUser(it) }.firstOrNull()
    }

    fun deleteByUserId(id: Long) = transaction {
        Users.deleteWhere {
            Users.id eq id
        }
    }

    fun updateConfirmInfo(id: Long, code: String) = transaction {
        Users.update({ Users.id eq id }) {
            it[currentRecoverCode] = code
            it[recoverCodeExpiredAt] = now().plusMinutes(3)
        }
    }

    fun updatePassword(id: Long, hashedPassword: String) = transaction {
        Users.update({ Users.id eq id }) {
            it[Users.hashedPassword] = hashedPassword
        }
    }

    fun updateEmailConfirmation(id: Long) = transaction {
        Users.update({ Users.id eq id }) {
            it[isEmailConfirmed] = true
        }
    }

    private fun mapToUser(resultRow: ResultRow): User = User(
        id = resultRow[id],
        username = resultRow[username],
        hashedPassword = resultRow[hashedPassword],
        currentRecoverCode = resultRow[currentRecoverCode],
        recoverCodeExpiredAt = resultRow[recoverCodeExpiredAt],
        isEmailConfirmed = resultRow[isEmailConfirmed]
    )
}