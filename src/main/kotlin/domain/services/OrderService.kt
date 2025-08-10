package domain.services

import domain.models.Order
import domain.models.utils.OrderStatus
import domain.models.utils.OrderType

// Interface for order service operations
interface OrderService {
    suspend fun createOrder(orderType: OrderType, employeeId: String, tableId: String? = null, customerId: String? = null): String
    suspend fun addItemToOrder(orderId: String, menuItemId: String, quantity: Int): Boolean
    suspend fun removeItemFromOrder(orderId: String, menuItemId: String): Boolean
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean
    suspend fun deleteOrder(orderId: String): Boolean
    suspend fun getOrderById(orderId: String): Order?
    suspend fun getOrdersByCustomer(customerId: String): List<Order>
    suspend fun listAllOrders(): List<Order>
}
