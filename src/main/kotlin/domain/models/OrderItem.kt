package domain.models

import domain.models.utils.OrderItemStatus
import java.math.BigDecimal

data class OrderItem(
    val id: String,
    val orderId: String,
    val menuItemId: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val status: OrderItemStatus = OrderItemStatus.PENDING,
) {
    val totalPrice: BigDecimal
        get() = unitPrice.multiply(BigDecimal(quantity))
}

