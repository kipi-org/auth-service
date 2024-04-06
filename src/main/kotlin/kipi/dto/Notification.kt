package kipi.dto

import java.io.Serializable

data class Notification(
    val beneficiary: String,
    val title: String,
    val message: String
) : Serializable