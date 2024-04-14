package kipi.utils


object ConfirmationUtils {
    fun generateCode(): String = (1000..9999).random().toString()
}