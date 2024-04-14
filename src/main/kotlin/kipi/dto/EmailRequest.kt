package kipi.dto

import java.io.Serializable

data class EmailRequest(
    val userId: Long,
    val email: String
) : Serializable