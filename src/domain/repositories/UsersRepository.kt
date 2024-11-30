package domain.repositories

import domain.dao.Users
import domain.dao.Users.id
import domain.dao.Users.phoneNumber
import domain.dto.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UsersRepository {

    fun createUser(user: User) = transaction {
        Users.insert {
            it[phoneNumber] = user.phoneNumber
        }[Users.id]
    }

    fun findUserByPhoneNumber(phoneNumber: String) = transaction {
        Users.select {
            Users.phoneNumber eq phoneNumber
        }.map { mapToUser(it) }.firstOrNull()
    }

    fun deleteByUserId(id: Long) = transaction {
        Users.deleteWhere {
            Users.id eq id
        }
    }

    private fun mapToUser(resultRow: ResultRow): User = User(
        id = resultRow[id],
        phoneNumber = resultRow[phoneNumber],
    )
}