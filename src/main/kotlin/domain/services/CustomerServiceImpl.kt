package domain.services

// dependencies
import data.repository.CustomerRepository
import domain.models.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {

    /*
     * @param name: String
     * @param phone: String
     * @param email: String?
     * @param address: String?
     * @param notes: String?
     * @return Boolean
     */
    override suspend fun addCustomer(name: String, phone: String, email: String?, address: String?, notes: String?): Boolean {
        return withContext(Dispatchers.IO) {
            if (name.isBlank() || phone.isBlank()) {
                throw IllegalArgumentException("Name and phone cannot be empty.")
            }
            if (customerRepository.findByPhone(phone) != null) {
                println("Customer with this phone number already exists.")
                return@withContext false
            }

            val newCustomer = Customer(
                id = UUID.randomUUID().toString(),
                name = name,
                phone = phone,
                email = email,
                address = address,
                notes = notes,
                isActive = true
            )
            delay(500) // Simulate DB latency
            return@withContext customerRepository.save(newCustomer)
        }
    }

    /*
     * @param id: String
     * @param name: String
     * @param phone: String
     * @param email: String
     * @return Boolean
     */
    override suspend fun addCustomerWithId(id: String, name: String, phone: String, email: String): Boolean = withContext(Dispatchers.IO) {
        if (name.isBlank() || phone.isBlank() || email.isBlank()) {
            throw IllegalArgumentException("Name, phone, and email cannot be empty.")
        }

        val newCustomer = Customer(
            id = id, // Use the provided user ID
            name = name,
            phone = phone,
            email = email,
            address = "", // Default empty address
            notes = "Customer profile created upon registration.",
            isActive = true
        )

        delay(500) // Simulate DB latency
        return@withContext customerRepository.save(newCustomer)
    }

    /*
     * @param id: String
     * @param name: String?
     * @param phone: String?
     * @param email: String?
     * @param address: String?
     * @param notes: String?
     * @return Boolean
     */
    override suspend fun updateCustomer(id: String, name: String?, phone: String?, email: String?, address: String?, notes: String?): Boolean {
        return withContext(Dispatchers.IO) {
            val existingCustomer = customerRepository.findById(id) ?: return@withContext false

            val updatedCustomer = existingCustomer.copy(
                name = name ?: existingCustomer.name,
                phone = phone ?: existingCustomer.phone,
                email = email ?: existingCustomer.email,
                address = address ?: existingCustomer.address,
                notes = notes ?: existingCustomer.notes
            )

            delay(500) // Simulate DB latency
            return@withContext customerRepository.save(updatedCustomer)
        }
    }

    /*
     * @param id: String
     * @return Boolean
     */
    override suspend fun deleteCustomer(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(500) // Simulate DB latency
            customerRepository.delete(id)
        }
    }

    /*
     * @param id: String
     * @return Customer?
     */
    override suspend fun getCustomerById(id: String): Customer? {
        return withContext(Dispatchers.IO) {
            delay(500) // Simulate DB latency
            customerRepository.findById(id)
        }
    }

    /*
     * @param phone: String
     * @return Customer?
     */
    override suspend fun getCustomerByPhone(phone: String): Customer? {
        return withContext(Dispatchers.IO) {
            delay(500) // Simulate DB latency
            customerRepository.findByPhone(phone)
        }
    }
    /*
     * @return List<Customer>
     */
    override suspend fun listAllCustomers(): List<Customer> {
        return withContext(Dispatchers.IO) {
            delay(500) // Simulate DB latency
            customerRepository.findAll()
        }
    }
}
