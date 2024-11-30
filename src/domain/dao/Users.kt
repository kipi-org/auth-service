package domain.dao

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = long("id").autoIncrement("user_id_seq")
    val phoneNumber = varchar("phoneNumber", 13)
    override val primaryKey = PrimaryKey(id)
}