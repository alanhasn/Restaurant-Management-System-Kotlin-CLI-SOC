package domain.services

import data.repository.CustomerRepository
import domain.models.Customer
import java.util.UUID

class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {

    override fun addCustomer(name: String, phone: String): Boolean {
        if (name.isBlank()) throw IllegalArgumentException("Name cannot be empty.")
        if (!phone.matches(Regex("\\d{10,15}"))) throw IllegalArgumentException("Invalid phone number.")

        val newCustomer = Customer(id = UUID.randomUUID().toString() , name = name, phone = phone)
        customerRepository.save(newCustomer)
        return true

    }

    override fun updateCustomer(id: String, name: String?, phone: String?): Boolean {
        val existingCustomer = customerRepository.findById(id) ?: return false

        val newName = if (!name.isNullOrBlank()) name else existingCustomer.name
        val newPhone = if (!phone.isNullOrBlank()) phone else existingCustomer.phone

        val updatedCustomer = existingCustomer.copy(name = newName, phone = newPhone)

        return customerRepository.update(updatedCustomer).isSuccess
    }

    override fun deleteCustomer(id: String): Boolean {
        val customer = customerRepository.findById(id)
            ?: throw IllegalArgumentException("Customer with id '$id' does not exist.")
        return customerRepository.delete(id)
    }

    override fun getCustomerById(id: String): Customer? {
        return customerRepository.findById(id)
            ?: throw IllegalArgumentException("Customer with id '$id' not found.")
    }

    override fun getCustomerByPhone(phone: String): Customer? {
        return customerRepository.findByPhone(phone)
            ?: throw IllegalArgumentException("Customer with phone '$phone' not found.")
    }

    override fun listAllCustomers(): List<Customer> {
        return customerRepository.findAll()
    }
}
