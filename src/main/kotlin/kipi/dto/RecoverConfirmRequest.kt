package kipi.dto

import java.io.Serializable

data class RecoverConfirmRequest(
    val userId: Long,
    val code: String,
    val newPassword: String
) : Serializable