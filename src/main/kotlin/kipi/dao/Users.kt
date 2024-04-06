package kipi.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Users : Table("users") {
    val id = long("id").autoIncrement("user_id_seq")
    val username = varchar("username", 30)
    val hashedPassword = varchar("hashedPassword", 60)
    val currentRecoverCode = varchar("currentRecoverCode", 4).nullable()
    val recoverCodeExpiredAt = datetime("recoverCodeExpiredAt").nullable()
    override val primaryKey = PrimaryKey(id)
}