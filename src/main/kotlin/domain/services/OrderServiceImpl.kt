package domain.services

import domain.models.Order
import domain.models.OrderItem
import domain.models.utils.OrderStatus
import domain.models.utils.OrderType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderServiceImpl(
    private val menuService: MenuService
) : OrderService {

    private val orders = mutableMapOf<String, Order>()

    override fun createOrder(
        tableId: String,
        customerId: String?,
        employeeId: String
    ): String {
        val id = UUID.randomUUID().toString()
        val newOrder = Order(
            id = id,
            customerId = customerId,
            tableId = tableId,
            status = OrderStatus.PENDING,
            orderType = OrderType.DINE_IN, // افتراضياً، يمكن توسيع المعاملات
            orderItems = mutableListOf(),
            orderTime = LocalDateTime.now(),
            totalAmount = BigDecimal.ZERO
        )
        orders[id] = newOrder
        return id
    }

    override fun addItemToOrder(orderId: String, menuItemId: String, quantity: Int): Boolean {
        val order = orders[orderId] ?: return false
        if (quantity <= 0) return false

        val unitPrice = menuService.getMenuItemById(menuItemId)?.price ?: return false

        val existingItem = order.orderItems.find { it.menuItemId == menuItemId }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            val newOrderItem = OrderItem(
                id = UUID.randomUUID().toString(),
                orderId = orderId,
                menuItemId = menuItemId,
                quantity = quantity,
                unitPrice = unitPrice
            )
            order.orderItems.add(newOrderItem)
        }

        var total = BigDecimal.ZERO
        for (item in order.orderItems) {
            val itemTotal = item.unitPrice.multiply(item.quantity.toBigDecimal())
            total = total.add(itemTotal)
        }
        orders[orderId] = order.copy(totalAmount = total)

        return true
    }


    override fun removeItemFromOrder(orderId: String, menuItemId: String): Boolean {
        val order = orders[orderId] ?: return false
        val removed = order.orderItems.removeIf { it.menuItemId == menuItemId }

        return removed
    }

    override fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orders[orderId] ?: return false
        orders[orderId] = order.copy(status = status)
        return true
    }

    override fun deleteOrder(orderId: String): Boolean {
        return orders.remove(orderId) != null
    }

    override fun getOrderById(orderId: String): Order? {
        return orders[orderId]
    }

    override fun listAllOrders(): List<Order> {
        return orders.values.toList()
    }
}
