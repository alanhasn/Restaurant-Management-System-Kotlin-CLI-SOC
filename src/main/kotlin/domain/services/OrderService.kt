package domain.services

import domain.models.Order
import domain.models.utils.OrderStatus


interface OrderService {
    fun createOrder(tableId: String, customerId: String?, employeeId: String): String // يرجع ID الطلب
    fun addItemToOrder(orderId: String, menuItemId: String, quantity: Int): Boolean
    fun removeItemFromOrder(orderId: String, menuItemId: String): Boolean
    fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean
    fun deleteOrder(orderId: String): Boolean
    fun getOrderById(orderId: String): Order?
    fun listAllOrders(): List<Order>
}
