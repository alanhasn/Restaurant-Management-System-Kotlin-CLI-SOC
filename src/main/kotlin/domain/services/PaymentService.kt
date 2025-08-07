package domain.services

import domain.models.Payment
import domain.models.utils.PaymentMethod


interface PaymentService {
    fun makePayment(orderId: String, amountPaid: Double, method: PaymentMethod): Boolean
    fun getPaymentById(id: String): Payment?
    fun getPaymentsForOrder(orderId: String): List<Payment>
    fun listAllPayments(): List<Payment>
}
