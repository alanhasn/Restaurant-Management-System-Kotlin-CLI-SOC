package domain.services

import domain.models.Customer

interface CustomerService {
    suspend fun addCustomer(name: String, phone: String, email: String?, address: String?, notes: String?): Boolean
    suspend fun addCustomerWithId(id: String, name: String, phone: String, email: String): Boolean
    suspend fun updateCustomer(id: String, name: String?, phone: String?, email: String?, address: String?, notes: String?): Boolean
    suspend fun deleteCustomer(id: String): Boolean
    suspend fun getCustomerById(id: String): Customer?
    suspend fun getCustomerByPhone(phone: String): Customer?
    suspend fun listAllCustomers(): List<Customer>
}
