package domain.services

import domain.models.Order
import domain.models.utils.OrderStatus


interface OrderService {
    suspend fun createOrder(tableId: String, customerId: String?, employeeId: String): String // يرجع ID الطلب
    suspend fun addItemToOrder(orderId: String, menuItemId: String, quantity: Int): Boolean
    suspend fun removeItemFromOrder(orderId: String, menuItemId: String): Boolean
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean
    suspend fun deleteOrder(orderId: String): Boolean
    suspend fun getOrderById(orderId: String): Order?
    suspend fun listAllOrders(): List<Order>
}
