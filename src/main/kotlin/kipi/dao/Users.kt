package kipi.dao

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = long("id").autoIncrement("user_id_seq")
    val username = varchar("username", 30)
    val hashedPassword = varchar("hashedPassword", 60)
    override val primaryKey = PrimaryKey(id)
}