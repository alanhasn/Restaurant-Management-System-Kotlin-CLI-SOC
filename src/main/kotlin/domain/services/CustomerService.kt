package domain.services

import domain.models.Customer


interface CustomerService {
    fun addCustomer(name: String, phone: String): Boolean
    fun updateCustomer(id: String, name: String?, phone: String?): Boolean
    fun deleteCustomer(id: String): Boolean
    fun getCustomerById(id: String): Customer?
    fun getCustomerByPhone(phone: String): Customer?
    fun listAllCustomers(): List<Customer>
}
