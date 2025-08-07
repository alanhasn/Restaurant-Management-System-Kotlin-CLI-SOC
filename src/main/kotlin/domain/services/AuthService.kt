package domain.services

import domain.models.User
import domain.models.utils.UserRole


interface AuthService {
    fun login(username: String , password: String): Boolean

    fun register(username: String , email: String , password: String , role: UserRole ): Boolean

    fun logout(userId: String): Boolean

    fun isAuthenticated(userId: String): Boolean

    fun getUserIdByUsername(username: String): String
}
