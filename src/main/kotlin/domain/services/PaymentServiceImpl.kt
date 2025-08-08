package domain.services

import domain.models.Payment
import domain.models.utils.PaymentMethod
import domain.models.utils.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class PaymentServiceImpl : PaymentService {

    private val payments = mutableMapOf<String, Payment>()

    override fun makePayment(
        orderId: String,
        amountPaid: Double,
        method: PaymentMethod
    ): Boolean {
        if (amountPaid <= 0) return false

        val payment = Payment(
            id = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = BigDecimal.valueOf(amountPaid),
            paymentMethod = method,
            status = PaymentStatus.COMPLETED,
            paymentDate = LocalDateTime.now()
        )

        payments[payment.id] = payment
        return true
    }

    override fun getPaymentById(id: String): Payment? {
        return payments[id]
    }

    override fun getPaymentsForOrder(orderId: String): List<Payment> {
        return payments.values.filter { it.orderId == orderId }
    }

    override fun listAllPayments(): List<Payment> {
        return payments.values.toList()
    }
}
