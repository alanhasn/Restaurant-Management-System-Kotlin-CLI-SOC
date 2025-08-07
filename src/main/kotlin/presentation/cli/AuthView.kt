package presentation.cli

import domain.models.utils.UserRole
import domain.services.AuthService
import exceptions.*

class AuthView(private val authService: AuthService) {

    fun showAuthMenu(): Pair<Boolean, String?> {
        while (true) {
            println("\n=== Restaurant Management System ===")
            println("1. Login\n2. Register\n3. Exit")
            print("Choose an option (1-3): ")
            when (readIntInRange(1..3)) {
                1 -> handleLogin()?.let { return true to it }
                2 -> handleRegistration()?.let { return true to it }
                3 -> {
                    println("\nGoodbye!")
                    return false to null
                }
            }
        }
    }

    private fun handleLogin(): String? {
        println("\n=== Login ===")
        repeat(2) { attempt ->
            print("Enter your Username: ")
            val username = readLine().orEmpty().trim()
            print("Enter your Password: ")
            val password = readPassword()

            try {
                if (authService.login(username, password)) {
                    println("\nWelcome back, $username!")
                    return authService.getUserIdByUsername(username)
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }

            println(if (attempt == 1) "\nToo many failed attempts.\n" else "\nTry again.")
        }
        return null
    }

    private fun handleRegistration(): String? {
        val usernameRegex = Regex("^[a-zA-Z0-9]{4,20}$")
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

        println("\n=== Register ===")
        try {
            val username = readValidated(
                prompt = "Enter your username (4–20 alphanum):",
                errorMessage = "Invalid username. Must be 4–20 alphanumeric.",
                validator = {  it.matches(usernameRegex) }
            )

            val email = readValidated(
                prompt = "Enter your email:",
                errorMessage = "Invalid email format.",
                validator = { it.matches(emailRegex)}
            )
            val password = readPasswordWithConfirmation()

            val role = chooseRole()

            if (authService.register(username, email, password, role)) {
                when (role.name.lowercase()){
                    "manager" -> {
                        println("\nYou have successfully registered as a Manager.")
                    }
                    "waiter" -> {
                        println("\nYou have successfully registered as a Waiter.")
                    }
                    "chef" -> {
                        println("\nYou have successfully registered as a Chef.")
                    }
                    "customer" -> {
                        println("\nYou have successfully registered as a Customer.")
                    }
                    else -> {
                        println("\nYou have successfully registered as a Customer.")
                    }
                }
                return authService.getUserIdByUsername(username)
            }

        } catch (e: EmailAlreadyUsedException) {
            println("Error: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        } catch (e: Exception) {
            println("Unexpected error: ${e.message}")
        }

        println("\nPress Enter to continue...")
        readLine()
        return null
    }

    // ================
    // Helper functions
    // ================
    private fun chooseRole(): UserRole {
        println("\nSelect your role:")
        UserRole.entries.forEachIndexed { i, role -> println("${i + 1}. ${role.name}") }
        print("Enter role number: ")
        val index = readIntInRange(1..UserRole.entries.size) - 1
        return UserRole.entries[index]
    }

    private fun readValidated(
        prompt: String,
        errorMessage: String = "Invalid input. Please try again.",
        validator: (String) -> Boolean
    ): String {
        while (true) {
            print("$prompt ")
            val input = readLine().orEmpty().trim()
            if (validator(input)) return input
            println(errorMessage)
        }
    }

    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            val input = readLine()
            val number = input?.toIntOrNull()
            if (number != null && number in range) return number
            println("Enter a number between ${range.first} and ${range.last}.")
        }
    }

    private fun readPassword(): String {
        val console = System.console()
        return if (console != null) {
            String(console.readPassword())
        } else {
            // Fallback: input not hidden (e.g., in IDEs)
            readLine().orEmpty()
        }.trim()
    }

    private fun readPasswordWithConfirmation(): String {
        while (true) {
            print("Password (min 8 chars): ")
            val password = readPassword()
            if (password.length < 8) {
                println("Password too short.")
                continue
            }

            print("Confirm Password: ")
            val confirm = readPassword()
            if (password == confirm) return password

            println("Passwords do not match.")
        }
    }
}
