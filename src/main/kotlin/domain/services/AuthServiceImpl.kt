package domain.services

import data.repository.UserRepository
import domain.models.User
import domain.models.utils.UserRole
import java.util.UUID
import at.favre.lib.crypto.bcrypt.BCrypt
import exceptions.EmailAlreadyUsedException
import exceptions.InvalidCredentialsException
import exceptions.UserNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import utils.isValidEmail
import utils.isValidUsername


class AuthServiceImpl(
    private val userRepository: UserRepository
): AuthService {

    private val failedLoginAttempts = mutableMapOf<String, Int>()
    private val maxAttempts = 3
    private val activeSessions = mutableSetOf<String>()

    override suspend fun login(username: String, password: String): Boolean {
        val user = userRepository.findAll().find { it.username == username }
            ?: throw UserNotFoundException("User with username '$username' not found.")

        // Check if user is locked out
        if (failedLoginAttempts.getOrDefault(username, 0) >= maxAttempts) {
            throw InvalidCredentialsException("Too many failed attempts for '$username'. Account temporarily locked.")
        }

        val result = BCrypt.verifyer().verify(password.toCharArray(), user.password)
        if (!result.verified) {
            failedLoginAttempts[username] = failedLoginAttempts.getOrDefault(username, 0) + 1
            throw InvalidCredentialsException("Incorrect password. Attempt ${failedLoginAttempts[username]}/$maxAttempts.")
        }

        // Success: reset failed attempts
        failedLoginAttempts.remove(username)
        activeSessions.add(user.id)
        return true
    }

    override suspend fun register(username: String, email: String, password: String, role: UserRole): Boolean {
        return withContext(Dispatchers.IO) {
            if (!isValidEmail(email)) throw IllegalArgumentException("Invalid email format.")
            if (!isValidUsername(username)) throw IllegalArgumentException("Username must be 4-20 alphanumeric characters.")

            if (userRepository.findByEmail(email) != null) {
                throw EmailAlreadyUsedException("Email '$email' is already registered.")
            }

            val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())

            val newUser = User(
                id = generateId(),
                username = username,
                email = email,
                password = hashedPassword,
                role = role
            )
            delay(1000)
            userRepository.save(newUser)
            return@withContext true

        }
    }

    override suspend fun logout(userId: String): Boolean {
        return activeSessions.remove(userId)

    }

    override suspend fun isAuthenticated(userId: String): Boolean {
        return userId in activeSessions
    }

    override suspend fun getUserIdByUsername(username: String): String {
        val user = userRepository.findAll().find { it.username == username }
            ?: throw UserNotFoundException("User not found.")
        return user.id
    }

    fun generateId(): String{
        return UUID.randomUUID().toString()
    }

}