package data.repository

import domain.models.User

interface UserRepository {
    fun save(user: User): User
    fun findById(id: String): User?
    fun findAll(): List<User>
    fun update(user: User): Result<User>
    fun delete(id: String): Boolean
}
