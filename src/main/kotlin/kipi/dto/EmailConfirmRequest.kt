package kipi.dto

import java.io.Serializable

data class EmailConfirmRequest(
    val userId: Long,
    val code: String
) : Serializable