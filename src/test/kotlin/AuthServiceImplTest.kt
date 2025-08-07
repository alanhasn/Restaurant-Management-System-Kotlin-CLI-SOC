import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import at.favre.lib.crypto.bcrypt.BCrypt
import data.repository.UserRepository
import domain.models.User
import domain.models.utils.UserRole
import domain.services.AuthServiceImpl
import exceptions.EmailAlreadyUsedException
import exceptions.InvalidCredentialsException
import exceptions.UserNotFoundException

class AuthServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var authService: AuthServiceImpl

    @BeforeEach
    fun setup() {
        userRepository = mockk(relaxed = true)
        authService = AuthServiceImpl(userRepository)
    }

    @Test
    fun `should register new user successfully`() {
        every { userRepository.findByEmail("test@example.com") } returns null
        every { userRepository.save(any()) } just Awaits

        val result = authService.register("Alan", "test@example.com", "password123", UserRole.CUSTOMER)

        assertTrue(result)
        verify { userRepository.save(match { it.email == "test@example.com" }) }
    }

    @Test
    fun `should throw exception for duplicate email`() {
        every { userRepository.findByEmail("test@example.com") } returns User(
            id = "1",
            username = "Alan",
            email = "test@example.com",
            password = "somehashed",
            role = UserRole.CUSTOMER
        )

        val ex = assertThrows<EmailAlreadyUsedException> {
            authService.register("Alan", "test@example.com", "password123", UserRole.CUSTOMER)
        }

        assertEquals("Email 'test@example.com' is already registered.", ex.message)
    }

    @Test
    fun `should login successfully with correct password`() {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, "password123".toCharArray())

        val user = User(
            id = "1",
            username = "Alan",
            email = "test@example.com",
            password = hashedPassword,
            role = UserRole.MANAGER
        )

        every { userRepository.findAll() } returns listOf(user)

        val result = authService.login("Alan", "password123")

        assertTrue(result)
    }

    @Test
    fun `should throw InvalidCredentialsException on wrong password`() {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, "correctpassword".toCharArray())

        val user = User(
            id = "1",
            username = "Alan",
            email = "test@example.com",
            password = hashedPassword,
            role = UserRole.CUSTOMER
        )

        every { userRepository.findAll() } returns listOf(user)

        val ex = assertThrows<InvalidCredentialsException> {
            authService.login("Alan", "wrongpassword")
        }

        assertTrue(ex.message!!.startsWith("Incorrect password"))
    }

    @Test
    fun `should throw UserNotFoundException if username doesn't exist`() {
        every { userRepository.findAll() } returns listOf()

        val ex = assertThrows<UserNotFoundException> {
            authService.login("NonUser", "pass")
        }

        assertEquals("User with username 'NonUser' not found.", ex.message)
    }

    @Test
    fun `should lock account after 5 failed attempts`() {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, "realpass".toCharArray())

        val user = User(
            id = "1",
            username = "Alan",
            email = "test@example.com",
            password = hashedPassword,
            role = UserRole.CUSTOMER
        )

        every { userRepository.findAll() } returns listOf(user)

        repeat(5) {
            assertThrows<InvalidCredentialsException> {
                authService.login("Alan", "wrong")
            }
        }

        val lockEx = assertThrows<InvalidCredentialsException> {
            authService.login("Alan", "wrong")
        }

        assertEquals("Too many failed attempts for 'Alan'. Account temporarily locked.", lockEx.message)
    }

    @Test
    fun `should reject invalid email format`() {
        val ex = assertThrows<IllegalArgumentException> {
            authService.register("Alan", "invalid_email", "password", UserRole.CUSTOMER)
        }

        assertEquals("Invalid email format.", ex.message)
    }

    @Test
    fun `should reject invalid username`() {
        val ex = assertThrows<IllegalArgumentException> {
            authService.register("!!", "test@example.com", "password", UserRole.CUSTOMER)
        }

        assertEquals("Username must be 4-20 alphanumeric characters.", ex.message)
    }
}
