package domain.services

import data.repository.CustomerRepository
import domain.models.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {

    override suspend fun addCustomer(name: String, phone: String): Boolean {
        return withContext(Dispatchers.IO){
            if (name.isBlank()) throw IllegalArgumentException("Name cannot be empty.")
            if (!phone.matches(Regex("\\d{10,15}"))) throw IllegalArgumentException("Invalid phone number.")

            val newCustomer = Customer(id = UUID.randomUUID().toString() , name = name, phone = phone)
            delay(1000)
            customerRepository.save(newCustomer)
            return@withContext true

        }
    }

    override suspend fun updateCustomer(id: String, name: String?, phone: String?): Boolean {
        return withContext(Dispatchers.IO){
            val existingCustomer = customerRepository.findById(id) ?: return@withContext false

            val newName = if (!name.isNullOrBlank()) name else existingCustomer.name
            val newPhone = if (!phone.isNullOrBlank()) phone else existingCustomer.phone

            val updatedCustomer = existingCustomer.copy(name = newName, phone = newPhone)
            delay(1000)
            return@withContext customerRepository.update(updatedCustomer).isSuccess

        }
    }

    override suspend fun deleteCustomer(id: String): Boolean {
        return withContext(Dispatchers.IO){        val customer = customerRepository.findById(id)
            ?: throw IllegalArgumentException("Customer with id '$id' does not exist.")
            delay(1000)
            return@withContext customerRepository.delete(id)
        }
    }

    override suspend fun getCustomerById(id: String): Customer? {
        return customerRepository.findById(id)
            ?: throw IllegalArgumentException("Customer with id '$id' not found.")
    }

    override suspend fun getCustomerByPhone(phone: String): Customer? {
        return customerRepository.findByPhone(phone)
            ?: throw IllegalArgumentException("Customer with phone '$phone' not found.")
    }

    override suspend fun listAllCustomers(): List<Customer> {
        delay(2000)
        return customerRepository.findAll()
    }
}
