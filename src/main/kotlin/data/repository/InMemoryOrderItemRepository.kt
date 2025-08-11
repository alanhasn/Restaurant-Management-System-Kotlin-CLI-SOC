package data.repository

// Dependencies
import domain.models.OrderItem
import domain.models.utils.OrderItemStatus
import java.util.UUID // For OrderItem ID generation

// interface OrderItemRepository for order item operations
class InMemoryOrderItemRepository: OrderItemRepository{

    // list of order items
    private val orderItems = mutableMapOf<String , OrderItem>()

    /**
     * Save an order item
     * @param orderItem: OrderItem
     * @return: OrderItem
     */
    override fun save(orderItem: OrderItem): OrderItem {
        val id = orderItem.id.ifBlank { UUID.randomUUID().toString() }
        val orderItemWithId = orderItem.copy(id=id)
        orderItems[id] = orderItemWithId
        return orderItemWithId
    }

    /**
     * Find an order item by id
     * @param id: String
     * @return: OrderItem?
     */
    override fun findById(id: String): OrderItem? {
        return orderItems[id]
    }

    /**
     * Find order items by order id
     * @param orderId: String
     * @return: List<OrderItem>
     */
    override fun findByOrderId(orderId: String): List<OrderItem> {
        return orderItems.values.filter { it.orderId == orderId }
    }

    /**
     * Find order items by menu item id
     * @param menuItemId: String
     * @return: List<OrderItem>
     */
    override fun findByMenuItemId(menuItemId: String): List<OrderItem> {
        return orderItems.values.filter { it.menuItemId == menuItemId }
    }

    /**
     * Find order items by status
     * @param status: OrderItemStatus
     * @return: List<OrderItem>
     */
    override fun findByStatus(status: OrderItemStatus): List<OrderItem> {
        return orderItems.values.filter { it.status == status }
    }

    /**
     * Update an order item
     * @param orderItem: OrderItem
     * @return: Result<OrderItem>
     */
    override fun update(orderItem: OrderItem): Result<OrderItem> {
        val id = orderItem.id
        if (!orderItems.containsKey(id)){
            return Result.failure(IllegalArgumentException("No order item found with id: $id"))
        }
        orderItems[id] = orderItem
        return Result.success(orderItem)
    }

    /**
     * Delete an order item by id
     * @param id: String
     * @return: Boolean
     */
    override fun delete(id: String): Boolean {
       return orderItems.remove(id) != null
    }

    /**
     * Delete order items by order id
     * @param orderId: String
     * @return: Boolean
     */
    override fun deleteByOrderId(orderId: String): Boolean {
        val initialSize = orderItems.size
        orderItems.entries.removeIf { it.value.orderId == orderId }
        return orderItems.size < initialSize
    }

}