package kipi.utils


object RecoverPasswordUtils {
    fun generateCode(): String = (1000..9999).random().toString()
}