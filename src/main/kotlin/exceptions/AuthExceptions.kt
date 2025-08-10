package exceptions


// Auth Exceptions Class Definitions for Authentication Service Exceptions
class InvalidCredentialsException(message: String) : Exception(message)
class EmailAlreadyUsedException(message: String) : Exception(message)
class UsernameAlreadyUsedException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)