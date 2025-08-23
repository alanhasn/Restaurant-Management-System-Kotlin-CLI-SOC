package data.repository

// Dependencies
import domain.models.Customer
import java.util.UUID // For Customer ID generation
import kotlin.collections.set

// In-Memory Customer Repository (Implementation of CustomerRepository interface)
class InMemoryCustomerRepository: CustomerRepository{

    // Mutable map to store customers
    private val customers = mutableMapOf<String, Customer>()

    // adding initial data for customers in the restaurant
    init {
        val customer1 = Customer(
            id = UUID.randomUUID().toString(),
            name = "Alan hassan",
            phone = "1234567890",
            email = "q5Oo6@example.com",
            address = "123 Main St",
            notes = "Customer profile created upon registration.",
            isActive = true
        )
        customers[customer1.id] = customer1

        val customer2 = Customer(
            id = UUID.randomUUID().toString(),
            name = "Ali",
            phone = "1234567890",
            email = "q5Oo6@example.com",
            address = "123 Main St",
            notes = "Customer profile created upon registration.",
            isActive = true
        )
        customers[customer2.id] = customer2

        val customer3 = Customer(
            id = UUID.randomUUID().toString(),
            name = "Ahmed alo",
            phone = "1234567890",
            email = "q5Oo6@example.com",
            address = "123 Main St",
            notes = "Customer profile created upon registration.",
            isActive = true
        )
        customers[customer3.id] = customer3

        val customer4 = Customer(
            id = UUID.randomUUID().toString(),
            name = "Ahmed",
            phone = "1234567890",
            email = "q5Oo6@example.com",
            address = "123 Main St",
            notes = "Customer profile created upon registration.",
            isActive = true
        )
        customers[customer4.id] = customer4
    }
    /**
     * Save a customer to the repository.
     * @param customer The customer to save.
     * @return True if the customer was saved successfully, false otherwise.
     */
    override fun save(customer: Customer): Boolean {
        // Create a new User instance with the generated or existing ID
        val id = customer.id.ifBlank { UUID.randomUUID().toString() }
        val customerWithId = customer.copy(id=id)

        customers[id] = customerWithId
        return true
    }

    /**
     * Find a customer by ID.
     * @param id The ID of the customer to find.
     * @return The customer with the specified ID, or null if not found.
     */
    override fun findById(id: String): Customer? {
        return customers[id]
    }

    /**
     * List all customers in the repository.
     * @return A list of all customers in the repository.
     */
    override fun findAll(): List<Customer> {
        return customers.values.toList()
    }

    /**
     * Update a customer in the repository.
     * @param customer The customer to update.
     * @return The updated customer, or null if not found.
     */
    override fun update(customer: Customer): Result<Customer> {
        val id = customer.id // Get the customer ID
        // Check if the customer exists
        if (!customers.containsKey(id)){
            return Result.failure(IllegalArgumentException("No customer found with id: $id"))
        }
        customers[id] = customer
        return Result.success(customer)
    }

    /**
     * Delete a customer from the repository.
     * @param id The ID of the customer to delete.
     * @return True if the customer was deleted successfully, false otherwise.
     */
    override fun delete(id: String): Boolean {
        return customers.remove(id) != null
    }

    /**
     * Find a customer by phone number.
     * @param phone The phone number of the customer to find.
     * @return The customer with the specified phone number, or null if not found.
     */
    override fun findByPhone(phone: String): Customer? {
        return customers.values.firstOrNull { it.phone == phone }
    }

    /**
     * Find a customer by email address.
     * @param email The email address of the customer to find.
     * @return The customer with the specified email address, or null if not found.
     */
    override fun findByEmail(email: String): Customer? {
        return customers.values.firstOrNull { it.email == email }
    }

}