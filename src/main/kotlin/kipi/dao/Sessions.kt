package kipi.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Sessions : Table("sessions") {
    val id = long("id").autoIncrement("session_id_seq")
    val userId = reference("userId", Users.id)
    val token = varchar("token", 255)
    val expiredAt = datetime("expiredAt")
    val initAt = datetime("initAt")
    override val primaryKey = PrimaryKey(id)
}