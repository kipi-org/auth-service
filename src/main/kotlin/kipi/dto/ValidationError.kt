package kipi.dto

import java.io.Serializable

data class ValidationError(
    val field: String,
    val message: String?
) : Serializable