package domain.models

import domain.models.utils.PaymentMethod
import domain.models.utils.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

/*
 * Payment class represents a payment made by a customer for an order.
 * It contains information about the payment, including the order ID, amount, payment method, status, and payment date.
 */
data class Payment(
    val id: String,
    val orderId: String,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val paymentDate: LocalDateTime = LocalDateTime.now(),
    val cardType: String? = null,
)