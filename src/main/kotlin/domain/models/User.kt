package domain.models

import domain.models.utils.UserRole

/*
 * User model class representing a user entity in the system with various properties and default values
*/
data class User(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val role: UserRole,
    val isActive: Boolean = true
)

