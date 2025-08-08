package domain.services

import domain.models.Payment
import domain.models.utils.PaymentMethod


interface PaymentService {
    suspend fun makePayment(orderId: String, amountPaid: Double, method: PaymentMethod): Boolean
    suspend fun getPaymentById(id: String): Payment?
    suspend fun getPaymentsForOrder(orderId: String): List<Payment>
    suspend fun listAllPayments(): List<Payment>
}
