package domain.models

import domain.models.utils.OrderStatus
import domain.models.utils.OrderType
import java.math.BigDecimal
import java.time.LocalDateTime

/*
 * Order class represents a customer's order.
 * It contains information about the order, including the order type, order items, and total amount.
 */
data class Order(
    val id: String,
    val customerId: String? = null,
    val tableId: String? = null,
    val status: OrderStatus = OrderStatus.PENDING,
    val orderType: OrderType,
    val orderItems: MutableList<OrderItem> = mutableListOf(),
    val orderTime: LocalDateTime = LocalDateTime.now(),
    val totalAmount: BigDecimal = BigDecimal.ZERO,
    val estimatedReadyTime: LocalDateTime? = null,
    val actualDeliveryTime: LocalDateTime? = null
)


