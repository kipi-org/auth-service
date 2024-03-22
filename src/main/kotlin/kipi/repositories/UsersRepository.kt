package kipi.repositories

import kipi.dao.Users
import kipi.dao.Users.hashedPassword
import kipi.dao.Users.id
import kipi.dao.Users.username
import kipi.dto.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UsersRepository {

    fun createUser(user: User) = transaction {
        Users.insert {
            it[username] = user.username
            it[hashedPassword] = user.hashedPassword
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

    private fun mapToUser(resultRow: ResultRow): User = User(
        id = resultRow[id],
        username = resultRow[username],
        hashedPassword = resultRow[hashedPassword]
    )
}