package kipi.dto

import java.time.LocalDateTime

data class Session(
    val id: Long?,
    val userId: Long,
    val token: String,
    val expiredAt: LocalDateTime,
    val initAt: LocalDateTime
)
