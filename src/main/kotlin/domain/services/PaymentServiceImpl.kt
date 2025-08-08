package domain.services

import domain.models.Payment
import domain.models.utils.PaymentMethod
import domain.models.utils.PaymentStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class PaymentServiceImpl : PaymentService {

    private val payments = mutableMapOf<String, Payment>()

    override suspend fun makePayment(
        orderId: String,
        amountPaid: Double,
        method: PaymentMethod
    ): Boolean = withContext(Dispatchers.IO){
        if (amountPaid <= 0) return@withContext false

        val payment = Payment(
            id = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = BigDecimal.valueOf(amountPaid),
            paymentMethod = method,
            status = PaymentStatus.COMPLETED,
            paymentDate = LocalDateTime.now()
        )
        delay(1000)
        payments[payment.id] = payment
        return@withContext true
    }

    override suspend fun getPaymentById(id: String): Payment? {
        delay(1000)
        return payments[id]
    }

    override suspend fun getPaymentsForOrder(orderId: String): List<Payment> {
        delay(1000)
        return payments.values.filter { it.orderId == orderId }
    }

    override suspend fun listAllPayments(): List<Payment> {
        delay(1000)
        return payments.values.toList()
    }
}
