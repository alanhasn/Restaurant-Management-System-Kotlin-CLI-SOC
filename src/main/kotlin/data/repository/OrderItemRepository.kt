package data.repository

import domain.models.OrderItem
import domain.models.utils.OrderItemStatus

// interface OrderItemRepository for order item operations
interface OrderItemRepository {
    fun save(orderItem: OrderItem): OrderItem
    fun findById(id: String): OrderItem?
    fun findByOrderId(orderId: String): List<OrderItem>
    fun findByMenuItemId(menuItemId: String): List<OrderItem>
    fun findByStatus(status: OrderItemStatus): List<OrderItem>
    fun update(orderItem: OrderItem): Result<OrderItem>
    fun delete(id: String): Boolean
    fun deleteByOrderId(orderId: String): Boolean
}
