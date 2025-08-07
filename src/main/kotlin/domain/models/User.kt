package domain.models

import domain.models.utils.UserRole

data class User(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val role: UserRole,
    val isActive: Boolean = true
)


