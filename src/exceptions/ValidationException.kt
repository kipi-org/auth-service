package exceptions

import domain.dto.ValidationError


class ValidationException(val errors: List<ValidationError>) : RuntimeException()