package domain.services


interface AuthService {
    fun login(email: String, password: String): Boolean

    fun register(email: String, password: String, name: String): Boolean

    fun logout(userId: String): Boolean

    fun isAuthenticated(userId: String): Boolean
}
