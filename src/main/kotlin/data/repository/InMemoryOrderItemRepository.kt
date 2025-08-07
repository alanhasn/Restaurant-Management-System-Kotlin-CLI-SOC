package data.repository

import domain.models.OrderItem
import domain.models.utils.OrderItemStatus
import java.util.UUID


class InMemoryOrderItemRepository: OrderItemRepository{
    private val orderItems = mutableMapOf<String , OrderItem>()

    override fun save(orderItem: OrderItem): OrderItem {
        val id = orderItem.id.ifBlank { UUID.randomUUID().toString() }
        val orderItemWithId = orderItem.copy(id=id)
        orderItems[id] = orderItemWithId
        return orderItemWithId
    }

    override fun findById(id: String): OrderItem? {
        return orderItems[id]
    }

    override fun findByOrderId(orderId: String): List<OrderItem> {
        return orderItems.values.filter { it.orderId == orderId }
    }

    override fun findByMenuItemId(menuItemId: String): List<OrderItem> {
        return orderItems.values.filter { it.menuItemId == menuItemId }
    }

    override fun findByStatus(status: OrderItemStatus): List<OrderItem> {
        return orderItems.values.filter { it.status == status }
    }

    override fun update(orderItem: OrderItem): Result<OrderItem> {
        val id = orderItem.id
        if (!orderItems.containsKey(id)){
            return Result.failure(IllegalArgumentException("No order item found with id: $id"))
        }
        orderItems[id] = orderItem
        return Result.success(orderItem)
    }

    override fun delete(id: String): Boolean {
       return orderItems.remove(id) != null
    }

    override fun deleteByOrderId(orderId: String): Boolean {
        val initialSize = orderItems.size
        orderItems.entries.removeIf { it.value.orderId == orderId }
        return orderItems.size < initialSize
    }

}