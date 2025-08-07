package data.repository

import domain.models.Payment
import domain.models.utils.PaymentStatus
import java.util.UUID


class InMemoryPaymentRepository: PaymentRepository{
    private val payments = mutableMapOf<String , Payment>()

    override fun save(payment: Payment): Payment {
        val id = payment.id.ifBlank { UUID.randomUUID().toString() }
        val paymentWithId = payment.copy(id=id)
        payments[id] = paymentWithId
        return paymentWithId
    }

    override fun findById(id: String): Payment? {
        return payments[id]
    }

    override fun findByOrderId(orderId: String): List<Payment> {
        return payments.values.filter { it.orderId == orderId }
    }

    override fun findByStatus(status: PaymentStatus): List<Payment> {
        return payments.values.filter { it.status == status }
    }

    override fun update(payment: Payment): Result<Payment> {
        val id = payment.id
        if (!payments.containsKey(id)){
            return Result.failure(IllegalArgumentException("No Payment found with id: $id"))
        }
        payments[id] = payment
        return Result.success(payment)
    }

    override fun delete(id: String): Boolean {
        return payments.remove(id) != null
    }
}