package kipi.exceptions

import kipi.dto.ValidationError

class ValidationException(val errors: List<ValidationError>) : RuntimeException()