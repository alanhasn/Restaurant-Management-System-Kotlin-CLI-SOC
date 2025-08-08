package domain.services

import domain.models.Order
import domain.models.OrderItem
import domain.models.utils.OrderStatus
import domain.models.utils.OrderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderServiceImpl(
    private val menuService: MenuService
) : OrderService {

    private val orders = mutableMapOf<String, Order>()

    override suspend fun createOrder(
        tableId: String,
        customerId: String?,
        employeeId: String
    ): String = withContext(Dispatchers.IO){
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
        delay(1000)
        orders[id] = newOrder
        return@withContext id
    }

    override suspend fun addItemToOrder(orderId: String, menuItemId: String, quantity: Int): Boolean =withContext(Dispatchers.IO) {
        val order = orders[orderId] ?: return@withContext false
        if (quantity <= 0) return@withContext false

        val unitPrice = menuService.getMenuItemById(menuItemId)?.price ?: return@withContext false

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
        delay(1000)
        orders[orderId] = order.copy(totalAmount = total)

        return@withContext true
    }


    override suspend fun removeItemFromOrder(orderId: String, menuItemId: String): Boolean {
        val order = orders[orderId] ?: return false
        val removed = order.orderItems.removeIf { it.menuItemId == menuItemId }
        delay(1000)
        return removed
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean =withContext(Dispatchers.IO){
        val order = orders[orderId] ?: return@withContext false
        delay(1000)
        orders[orderId] = order.copy(status = status)
        return@withContext true
    }

    override suspend fun deleteOrder(orderId: String): Boolean {
        delay(1000)
        return orders.remove(orderId) != null
    }

    override suspend fun getOrderById(orderId: String): Order? {
        delay(1000)
        return orders[orderId]
    }

    override suspend fun listAllOrders(): List<Order> {
        delay(1000)
        return orders.values.toList()
    }
}
