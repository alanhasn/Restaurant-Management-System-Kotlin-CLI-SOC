package data.repository

import domain.models.Order
import domain.models.utils.OrderStatus
import java.time.LocalDateTime

interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: String): Order?
    fun findByStatus(status: OrderStatus): List<Order>
    fun findByCustomer(customerId: String): List<Order>
    fun findByTable(tableId: String): List<Order>
    fun update(order: Order): Result<Order>
    fun delete(id: String): Boolean
}
