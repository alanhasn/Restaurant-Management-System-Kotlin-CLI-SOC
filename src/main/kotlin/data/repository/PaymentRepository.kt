package data.repository

import domain.models.Payment
import domain.models.utils.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

interface PaymentRepository {
    fun save(payment: Payment): Payment
    fun findById(id: String): Payment?
    fun findByOrderId(orderId: String): List<Payment>
    fun findByStatus(status: PaymentStatus): List<Payment>
    fun update(payment: Payment): Result<Payment>
    fun delete(id: String): Boolean
}
