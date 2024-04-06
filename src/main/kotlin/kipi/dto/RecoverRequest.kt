package kipi.dto

import java.io.Serializable

data class RecoverRequest(
    val userId: Long,
    val email: String
) : Serializable