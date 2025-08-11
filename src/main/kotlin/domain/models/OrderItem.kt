package domain.models

import domain.models.utils.OrderItemStatus
import java.math.BigDecimal

/*
 * OrderItem is a data class representing an item in an order
 */
data class OrderItem(
    val id: String,
    val orderId: String,
    val menuItemId: String,
    var quantity: Int,
    val unitPrice: BigDecimal,
    val status: OrderItemStatus = OrderItemStatus.PENDING,
)

