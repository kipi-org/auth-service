package kipi.dto

import java.io.Serializable
import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val username: String,
    val hashedPassword: String,
    val sessions: List<Session> = emptyList(),
    val currentRecoverCode: String? = null,
    val recoverCodeExpiredAt: LocalDateTime? = null,
    val isEmailConfirmed: Boolean = false
) : Serializable
