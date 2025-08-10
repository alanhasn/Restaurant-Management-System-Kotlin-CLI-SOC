package data.repository

import domain.models.Payment
import domain.models.utils.PaymentStatus

// Interface for payment repository
interface PaymentRepository {
    fun save(payment: Payment): Boolean
    fun findAll(): List<Payment>
    fun findById(id: String): Payment?
    fun findByOrderId(orderId: String): List<Payment>
    fun findByStatus(status: PaymentStatus): List<Payment>
    fun update(payment: Payment): Result<Payment>
    fun delete(id: String): Boolean
}
