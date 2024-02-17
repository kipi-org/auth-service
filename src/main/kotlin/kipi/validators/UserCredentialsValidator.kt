package kipi.validators

import kipi.dto.Credentials
import kipi.dto.ValidationError
import kipi.exceptions.ValidationException

object UserCredentialsValidator {
    private val USERNAME_PATTERN = "[\\da-zA-Z_-]{2,20}".toRegex()
    private val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$".toRegex()

    fun validate(credentials: Credentials) {
        val errors = ArrayList<ValidationError>()

        if (!credentials.username.matches(USERNAME_PATTERN))
            errors.add(ValidationError("username", "auth.param.incorrect.format"))

        if (!credentials.password.matches(PASSWORD_PATTERN))
            errors.add(ValidationError("password", "auth.param.incorrect.format"))

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}