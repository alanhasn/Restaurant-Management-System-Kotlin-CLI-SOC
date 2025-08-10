package data.repository

import domain.models.Payment
import domain.models.utils.PaymentMethod
import domain.models.utils.PaymentStatus
import java.math.BigDecimal

// Interface for payment repository
interface PaymentRepository {
    fun save(payment: Payment): Boolean
    fun findAll(): List<Payment>
    fun findById(id: String): Payment?
    fun findByOrderId(orderId: String): List<Payment>
    fun payBill(orderId: String, amount: BigDecimal, method: PaymentMethod): Result<Payment>
    fun findByStatus(status: PaymentStatus): List<Payment>
    fun update(payment: Payment): Result<Payment>
    fun delete(id: String): Boolean
}
