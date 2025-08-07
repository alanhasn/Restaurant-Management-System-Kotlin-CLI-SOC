package data.repository

import domain.models.Customer

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(id: String): Customer?
    fun findAll(): List<Customer>
    fun update(customer: Customer): Result<Customer>
    fun delete(id: String): Boolean
    fun findByPhone(phone: String): Customer?
    fun findByEmail(email: String): Customer?
}
