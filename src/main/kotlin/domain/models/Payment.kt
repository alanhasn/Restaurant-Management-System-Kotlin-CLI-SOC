package domain.models

import domain.models.utils.PaymentMethod
import domain.models.utils.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class Payment(
    val id: String,
    val orderId: String,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val paymentDate: LocalDateTime = LocalDateTime.now(),
    val cardType: String? = null,
)