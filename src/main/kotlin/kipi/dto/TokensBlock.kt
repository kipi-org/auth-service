package kipi.dto

import java.io.Serializable

data class TokensBlock(
    val accessSession: Session,
    val refreshSession: Session
) : Serializable
