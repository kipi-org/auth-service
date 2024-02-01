package kipi.dto

import java.io.Serializable

data class IdCredentials(
    val id: Long,
    val password: String
) : Serializable