// UserRepository
package data.repository

import domain.models.User
import java.util.UUID

class InMemoryUserRepository: UserRepository{

    // Map for Storing Users
    private val users = mutableMapOf<String , User>()

    override fun save(user: User): User {
        // Create a new User instance with the generated or existing ID
        val id = user.id.ifBlank { UUID.randomUUID().toString() }

        // Create a new User with ID
        val userWithId = user.copy(id=id)

        // Save the user
        users[id] = userWithId
        return userWithId
    }

    override fun findById(id: String): User? {
        return users[id]
    }

    override fun findAll(): List<User> {
        return users.values.toList()
    }

    override fun findByEmail(email: String): User? {
        return users.values.firstOrNull { it.email == email }
    }

    override fun update(user: User): Result<User> {
        val id = user.id // Gte the user ID
        if (!users.containsKey(id)){
            return Result.failure(IllegalArgumentException("No user found with id: $id"))
        }
        users[id] = user // Update with new info
        return Result.success(user)
    }

    override fun delete(id: String): Boolean {
        return users.remove(id) != null
    }

}