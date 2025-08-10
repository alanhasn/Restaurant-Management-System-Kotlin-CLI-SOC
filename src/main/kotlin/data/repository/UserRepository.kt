package data.repository

import domain.models.User

// interface for user repository
interface UserRepository {
    fun save(user: User): User
    fun findById(id: String): User?
    fun findAll(): List<User>
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun update(user: User): Result<User>
    fun delete(id: String): Boolean
}
