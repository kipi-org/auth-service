package domain.dto


data class TokensBlock(
    val accessSession: Session, val refreshSession: Session
)
