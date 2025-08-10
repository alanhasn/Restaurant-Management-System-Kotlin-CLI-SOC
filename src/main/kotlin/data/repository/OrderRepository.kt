package data.repository

import domain.models.Order
import domain.models.utils.OrderStatus

// interface for order repository
interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: String): Order?
    fun findByStatus(status: OrderStatus): List<Order>
    fun findByCustomer(customerId: String): List<Order>
    fun findByTable(tableId: String): List<Order>
    fun findAll(): List<Order>
    fun update(order: Order): Boolean
    fun delete(id: String): Boolean
}
