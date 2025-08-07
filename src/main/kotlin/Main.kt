import data.repository.InMemoryUserRepository
import domain.services.AuthServiceImpl
import presentation.cli.AuthView

fun main() {
    val userRepository = InMemoryUserRepository()
    val authService = AuthServiceImpl(userRepository)
    val authView = AuthView(authService)

    val (isAuthenticated, userId) = authView.showAuthMenu()
    if (isAuthenticated && userId != null) {
        // User is authenticated, proceed to main application
        println("User $userId has logged in successfully!")
        // Show main application menu here
    }
}

