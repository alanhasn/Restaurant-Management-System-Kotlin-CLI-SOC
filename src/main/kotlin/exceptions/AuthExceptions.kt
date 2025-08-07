package exceptions

class UserNotFoundException(message: String) : Exception(message)
class InvalidCredentialsException(message: String) : Exception(message)
class EmailAlreadyUsedException(message: String) : Exception(message)
