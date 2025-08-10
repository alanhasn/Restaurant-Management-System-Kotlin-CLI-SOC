package domain.services

import domain.models.Payment
import domain.models.utils.PaymentMethod
import java.math.BigDecimal

// Interface for payment service operations
interface PaymentService {
    suspend fun makePayment(orderId: String, amountPaid: BigDecimal, method: PaymentMethod): Boolean
    suspend fun getPaymentById(id: String): Payment?
    suspend fun getPaymentsForOrder(orderId: String): List<Payment>
    suspend fun listAllPayments(): List<Payment>
}
