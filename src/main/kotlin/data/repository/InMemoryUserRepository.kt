package data.repository

// Dependencies
import at.favre.lib.crypto.bcrypt.BCrypt // For Password Hashing
import domain.models.User
import domain.models.utils.UserRole
import java.util.UUID // For User ID generation

// In-Memory User Repository (Implementation of UserRepository Interface)
class InMemoryUserRepository: UserRepository{

    // Map for Storing Users
    private val users = mutableMapOf<String , User>()

    // Initialize Admin User
    init {
        val adminUser = User(
            id = UUID.randomUUID().toString(),
            username = "Whoamialanadmin",
            password = BCrypt.withDefaults().hashToString(12, "Whoami2011".toCharArray()),
            email = "Whoamialan@gmail.com",
            role = UserRole.ADMIN,
            isActive = true
        )
        val id = adminUser.id
        users[id] = adminUser // Save the user
    }

    /*
    * Save a new User
    * @param user: User to be saved
    * @return: Saved User
    */
    override fun save(user: User): User {
        // Create a new User instance with the generated or existing ID
        val id = user.id.ifBlank { UUID.randomUUID().toString() }

        // Create a new User with ID
        val userWithId = user.copy(id=id)

        // Save the user
        users[id] = userWithId
        return userWithId
    }

    /*
    * Find a User by ID
    * @param id: ID of the User to be found
    * @return: Found User or null if not found
    */
    override fun findById(id: String): User? {
        return users[id]
    }

    /*
    * Find all Users
    * @return: List of all Users
    */
    override fun findAll(): List<User> {
        return users.values.toList() // Return a list of all users
    }

    /*
    * Find a User by Email
    * @param email: Email of the User to be found
    * @return: Found User or null if not found
    */
    override fun findByEmail(email: String): User? {
        return users.values.firstOrNull { it.email == email }
    }

    /*
     * Update a User
     * @param user: User to be updated
     * @return: Updated User
     */
    override fun update(user: User): Result<User> {
        val id = user.id // Get the user ID
        if (!users.containsKey(id)){ // Check if user exists
            return Result.failure(IllegalArgumentException("No user found with id: $id"))
        }
        users[id] = user // Update with new info
        return Result.success(user) // Return the updated user
    }

    /*
     * Delete a User by ID
     * @param id: ID of the User to be deleted
     * @return: True if deleted, false if not found
     */
    override fun delete(id: String): Boolean {
        // Return true if deleted successfully else false
        return users.remove(id) != null
    }

    override fun findByUsername(username: String): User? =
        users.values.find { it.username.equals(username, ignoreCase = true) }
    }

