package domain.services

// Dependencies
import data.repository.UserRepository
import domain.models.User
import domain.models.utils.UserRole
import at.favre.lib.crypto.bcrypt.BCrypt
import exceptions.EmailAlreadyUsedException
import exceptions.InvalidCredentialsException
import exceptions.UserNotFoundException
import exceptions.UsernameAlreadyUsedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.isValidEmail
import utils.isValidUsername
import java.util.UUID

// Auth Service Implementation
class AuthServiceImpl(
    private val userRepository: UserRepository // User Repository Dependency Injection
) : AuthService {

    // Private Properties
    private val failedLoginAttempts = mutableMapOf<String, Int>() // Map to track failed login attempts
    private val maxAttempts = 3 // Maximum failed login attempts
    private val activeSessions = mutableSetOf<String>() // Set to track active sessions


    /**
     * Function to handle login
     * @param username: Username of the user
     * @param password: Password of the user
     */
    override suspend fun login(username: String, password: String): User? {
        // First, check if user exists directly from repository
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User with username '$username' not found.")

        // Check if account is locked
        if (failedLoginAttempts.getOrDefault(username, 0) >= maxAttempts) {
            throw InvalidCredentialsException("Too many failed attempts for '$username'. Account temporarily locked.")
        }

        // Verify password using BCrypt
        val result = BCrypt.verifyer().verify(password.toCharArray(), user.password)
        if (!result.verified) {
            // Increment failed attempts
            failedLoginAttempts[username] = failedLoginAttempts.getOrDefault(username, 0) + 1
            throw InvalidCredentialsException(
                "Incorrect password. Attempt ${failedLoginAttempts[username]}/$maxAttempts."
            )
        }

        // Successful login → reset failed attempts
        failedLoginAttempts.remove(username)

        // Add session (ensure ID is stored as String)
        activeSessions.add(user.id)

        return user
    }

    /**
     * Function to handle registration
     * @param username: Username of the user
     * @param email: Email of the user
     * @param password: Password of the user
     * @param role: Role of the user
     */
    override suspend fun register(
        username: String,
        email: String,
        password: String,
        role: UserRole
    ): User? = withContext(Dispatchers.IO) {

        // Validate email & username format
        if (!isValidEmail(email)) throw IllegalArgumentException("Invalid email format.")
        if (!isValidUsername(username)) throw IllegalArgumentException("Username must be 4-20 alphanumeric characters.")

        // Check if username or email already exists
        if (userRepository.findByUsername(username) != null) {
            throw UsernameAlreadyUsedException("Username '$username' is already taken.")
        }
        if (userRepository.findByEmail(email) != null) {
            throw EmailAlreadyUsedException("Email '$email' is already in use.")
        }

        // Hash password
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray()) // 12 work factor

        // Create new user object
        val newUser = User(
            id = UUID.randomUUID().toString(),
            username = username,
            email = email,
            password = hashedPassword,
            role = role
        )

        // Save and return the saved user (not Boolean)
        return@withContext userRepository.save(newUser)
    }

    override suspend fun logout(userId: String): Boolean {
        return activeSessions.remove(userId)
    }

    override suspend fun isAuthenticated(userId: String): Boolean {
        return activeSessions.contains(userId)
    }

    override suspend fun getUserIdByUsername(username: String): String {
        val user = userRepository.findAll().find { it.username == username}
            ?: throw UserNotFoundException("User not found.")
        return user.id
    }
}