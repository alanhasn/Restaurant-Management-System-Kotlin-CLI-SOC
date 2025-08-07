package data.repository

import domain.models.Customer
import java.util.UUID
import kotlin.collections.set

class InMemoryCustomerRepository: CustomerRepository{

    private val customers = mutableMapOf<String, Customer>()

    override fun save(customer: Customer): Customer {
        // Create a new customer instance with the generated or existing ID
        val id = customer.id.ifBlank { UUID.randomUUID().toString() }

        // Create a new Customer with ID
        val customerWithId = customer.copy(id=id)

        // Save the customer
        customers[id] = customerWithId
        return customerWithId
    }

    override fun findById(id: String): Customer? {
        return customers[id]
    }

    override fun findAll(): List<Customer> {
        return customers.values.toList()
    }

    override fun update(customer: Customer): Result<Customer> {
        val id = customer.id

        if (!customers.containsKey(id)){
            return Result.failure(IllegalArgumentException("No customer found with id: $id"))
        }
        customers[id] = customer
        return Result.success(customer)
    }

    override fun delete(id: String): Boolean {
        return customers.remove(id) != null
    }

    override fun findByPhone(phone: String): Customer? {
        return customers.values.firstOrNull { it.phone == phone }
    }

    override fun findByEmail(email: String): Customer? {
        return customers.values.firstOrNull { it.email == email }
    }

}