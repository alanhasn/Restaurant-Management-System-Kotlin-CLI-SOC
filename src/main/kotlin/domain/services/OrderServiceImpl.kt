package domain.services

// Dependencies
import data.repository.OrderRepository
import domain.models.Order
import domain.models.OrderItem
import domain.models.utils.OrderStatus
import domain.models.utils.OrderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

// Order Service Implementation
class OrderServiceImpl(
    // Dependencies injected
    private val orderRepository: OrderRepository,
    private val menuService: MenuService,
) : OrderService {

    /*
     * Creates a new order based on the provided order type, employee ID, table ID, and customer ID.
     * The order ID is generated using a UUID, and the order is saved to the repository.
     * @param orderType The type of order to create.
     * @param employeeId The ID of the employee creating the order.
     * @param tableId The ID of the table for DINE-IN orders, or null for TAKE-AWAY orders.
     * @param customerId The ID of the customer for DINE-IN orders, or null for TAKE-AWAY orders.
     * @return The ID of the newly created order.
     */
    override suspend fun createOrder(orderType: OrderType, employeeId: String, tableId: String?, customerId: String?): String {
        return withContext(Dispatchers.IO) {
            if (orderType == OrderType.DINE_IN && tableId.isNullOrBlank()) {
                throw IllegalArgumentException("Table ID is required for DINE-IN orders.")
            }

            val orderId = UUID.randomUUID().toString()
            val newOrder = Order(
                id = orderId,
                orderType = orderType,
                tableId = tableId,
                customerId = customerId,
                status = OrderStatus.PENDING,
                orderTime = LocalDateTime.now(),
                orderItems = mutableListOf(),
                totalAmount = BigDecimal.ZERO
            )

            orderRepository.save(newOrder)
            return@withContext orderId
        }
    }

    /*
     * Adds a new menu item to an existing order.
     * @param orderId The ID of the order to add the item to.
     * @param menuItemId The ID of the menu item to add.
     * @param quantity The quantity of the menu item to add.
     * @return true if the item was successfully added to the order, false otherwise.
     */
    override suspend fun addItemToOrder(orderId: String, menuItemId: String, quantity: Int): Boolean {
        return withContext(Dispatchers.IO) {
            // Retrieve the order and menu item
            val order = orderRepository.findById(orderId) ?: return@withContext false
            val menuItem = menuService.getMenuItemById(menuItemId) ?: return@withContext false
            // Validate the quantity and update the order
            if (quantity <= 0) throw IllegalArgumentException("Quantity must be positive.")

            val existingItem = order.orderItems.find { it.menuItemId == menuItemId } // Find the existing item
            if (existingItem != null) {
                existingItem.quantity += quantity
            } else {
                // Add the item to the order
                order.orderItems.add(
                    OrderItem(
                        id = UUID.randomUUID().toString(),
                        orderId = orderId,
                        menuItemId = menuItemId,
                        quantity = quantity,
                        unitPrice = menuItem.price
                    )
                )
            }
            // Update the total amount
            val newTotalAmount = order.orderItems.sumOf { it.unitPrice.multiply(BigDecimal.valueOf(it.quantity.toLong())) }
            val updatedOrder = order.copy(totalAmount = newTotalAmount)

            orderRepository.update(updatedOrder)
        }
    }

    override suspend fun removeItemFromOrder(orderId: String, menuItemId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val order = orderRepository.findById(orderId) ?: return@withContext false
            val itemRemoved = order.orderItems.removeIf { it.menuItemId == menuItemId }
            if (!itemRemoved) return@withContext false

            val newTotalAmount = order.orderItems.sumOf { it.unitPrice.multiply(BigDecimal.valueOf(it.quantity.toLong())) }
            val updatedOrder = order.copy(totalAmount = newTotalAmount)

            orderRepository.update(updatedOrder)
        }
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        return withContext(Dispatchers.IO) {
            val order = orderRepository.findById(orderId) ?: return@withContext false
            val updatedOrder = order.copy(status = status)
            return@withContext orderRepository.update(updatedOrder)
        }
    }

    override suspend fun deleteOrder(orderId: String): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext orderRepository.delete(orderId)
        }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return withContext(Dispatchers.IO) {
            return@withContext orderRepository.findById(orderId)
        }
    }

    override suspend fun getOrdersByCustomer(customerId: String): List<Order> {
        return withContext(Dispatchers.IO) {
            return@withContext orderRepository.findByCustomer(customerId)
        }
    }

    override suspend fun listAllOrders(): List<Order> {
        return withContext(Dispatchers.IO) {
            return@withContext orderRepository.findAll()
        }
    }
}
