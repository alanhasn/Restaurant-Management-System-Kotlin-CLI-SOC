package presentation.cli

// Dependencies/
import domain.models.User
import domain.models.utils.UserRole
import domain.services.AuthService
import domain.services.CustomerService
import kotlinx.coroutines.runBlocking

class AuthView(
    // Dependencies kept private for encapsulation
    private val authService: AuthService,
    private val customerService: CustomerService
) {

    /**
     * Displays the authentication menu and handles user input.
     * @return The authenticated user, or null if login is canceled
     */
    fun showAuthMenu(): User? {
        while (true) {
            println("\n=== You should be logged in to use this feature  ===")
            println("1. Login")
            println("2. Register")
            println("3. Exit")

            when (readIntInRange(1..3)) {
                1 -> handleLogin()?.let { return it } // Return the authenticated user if login is successful
                2 -> handleRegistration()?.let { return it } // Return the authenticated user if registration is successful
                3 -> {
                    println("\nGoodbye!")
                    return null
                }
            }
        }
    }

    /**
     * Handles user login.
     * @return The authenticated user, or null if login is canceled
     */
    private fun handleLogin(): User? = runBlocking {
        println("\n=== Login ===")
        // Attempt login 3 times
        repeat(3) { attempt ->
            print("Enter your Username: ")
            val username = readLine().orEmpty().trim().lowercase()
            print("Enter your Password: ")
            val password = readPassword()

            try {
                val user = authService.login(username, password)
                if (user != null) {
                    return@runBlocking user
                }
            } catch (e: Exception) {
                println("Error during login: ${e.message}")
            }
            // Prompt user to try again
            if (attempt < 2) {
                println("Please try again.")
            } else {
                println("Too many failed attempts. Returning to main menu.")
            }
        }
        // If login fails 3 times, return null
        return@runBlocking null
    }

    /**
     * Handles user registration.
     * @return The registered user, or null if registration is canceled
     */
    private fun handleRegistration(): User? = runBlocking {
        println("\n=== Register New Account ===")
        try {
            val username = readValidated(
                prompt = "Enter your username (4–20 alphanumeric): ",
                errorMessage = "Invalid username. Must be 4–20 alphanumeric characters.",
                validator = { it.matches(Regex("^[a-zA-Z0-9]{4,20}$")) } // Username must be 4–20 alphanumeric
            )

            val email = readValidated(
                prompt = "Enter your email: ",
                errorMessage = "Invalid email format.",
                // Email must be valid
                validator = { it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) }
            )
            val password = readPasswordWithConfirmation()

            val role = chooseRole()

            val newUser = authService.register(username, email, password, role)
            if (newUser != null) {
                println("\nYou have successfully registered as a ${role.name.lowercase().replaceFirstChar { it.titlecase() }}.")

                if (role == UserRole.CUSTOMER) {
                    println("\nPlease provide a few more details to create your customer profile.")
                    val fullName = readNonEmptyInput("Enter your full name: ", "Full name cannot be empty.")
                    val phone = readNonEmptyInput("Enter your phone number: ", "Phone number cannot be empty.")

                    val customerCreated = customerService.addCustomerWithId(newUser.id, fullName, phone, newUser.email)
                    if (customerCreated) {
                        println("Your customer profile has been created successfully.")
                    } else {
                        println("Critical Error: Could not create your customer profile. Please contact support.")
                    }
                }
                return@runBlocking newUser
            }
            println("Registration failed. The username or email might already be taken.")
            return@runBlocking null
        } catch (e: Exception) {
            println("An error occurred during registration: ${e.message}")
            return@runBlocking null
        }
    }

    // prompt user to choose role
    private fun chooseRole(): UserRole {
        println("\nChoose your role:")
        val roles = UserRole.entries.filter { it != UserRole.ADMIN } // Prevent self-registration as ADMIN
        roles.forEachIndexed { index, role ->
            println("${index + 1}. ${role.name.lowercase().replaceFirstChar { it.titlecase() }}")
        }
        // Prompt user to choose a role
        val choice = readIntInRange(1..roles.size)
        return roles[choice - 1]
    }

    // read password to hide input from user
    private fun readPassword(): String {
        // Read password from console if available
        return System.console()?.readPassword()?.joinToString("") ?: readLine().orEmpty()
    }

    // read password with confirmation
    private fun readPasswordWithConfirmation(): String {
        while (true) {
            print("Enter your password: ")
            val password = readPassword()
            print("Confirm your password: ")
            val confirmation = readPassword()
            if (password == confirmation) {
                if (password.isNotBlank()) return password
                println("Password cannot be empty.")
            } else {
                println("\nPasswords do not match. Please try again.\n")
            }
        }
    }

    // read non-empty input
    private fun readNonEmptyInput(prompt: String, errorMessage: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (!input.isNullOrBlank()) return input
            println(errorMessage)
        }
    }

    // read validated input
    private fun readValidated(prompt: String, errorMessage: String, validator: (String) -> Boolean): String {
        while (true) {
            val input = readNonEmptyInput(prompt, "Input cannot be empty.")
            if (validator(input)) {
                return input
            }
            println(errorMessage)
        }
    }

    // read integer within a specified range
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            print("Enter your choice (${range.first}-${range.last}): ")
            try {
                val input = readLine()?.trim()?.toInt() ?: throw NumberFormatException()
                if (input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                println("Invalid input. Please enter a valid number.")
            }
        }
    }
}
