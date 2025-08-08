package domain.services

import domain.models.User
import domain.models.utils.UserRole


interface AuthService {
    suspend fun login(username: String , password: String): Boolean
    suspend fun register(username: String , email: String , password: String , role: UserRole ): Boolean
    suspend fun logout(userId: String): Boolean
    suspend fun isAuthenticated(userId: String): Boolean
    suspend fun getUserIdByUsername(username: String): String
}
