package domain.services

// Dependencies
import data.repository.PaymentRepository
import domain.models.Payment
import domain.models.utils.PaymentMethod
import domain.models.utils.PaymentStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal // Import the BigDecimal class for handling decimal values
import java.time.LocalDateTime
import java.util.UUID

// Payment Service Implementation
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository
) : PaymentService {

    /*
     * Make a payment for an order
     * @param orderId The ID of the order to make the payment for
     * @param amountPaid The amount of the payment
     * @param method The payment method used
     * @return true if the payment was successful, false otherwise
     */
    override suspend fun makePayment(
        orderId: String,
        amountPaid: BigDecimal,
        method: PaymentMethod
    ): Boolean = withContext(Dispatchers.IO) {
        if (amountPaid <= BigDecimal.ZERO) return@withContext false // Payment amount must be positive

        // Simulate payment processing
        val payment = Payment(
            id = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = amountPaid,
            paymentMethod = method,
            status = PaymentStatus.COMPLETED,
            paymentDate = LocalDateTime.now()
        )
        delay(500) // Simulate DB latency
        return@withContext paymentRepository.save(payment)
    }

    /*
     * Retrieve a payment by its ID
     * @param id The ID of the payment to retrieve
     * @return The payment object if found, null otherwise
     */
    override suspend fun getPaymentById(id: String): Payment? = withContext(Dispatchers.IO) {
        return@withContext paymentRepository.findById(id)
    }

    /*
     * Retrieve all payments for an order
     * @param orderId The ID of the order to retrieve payments for
     * @return A list of payments for the order
     */
    override suspend fun getPaymentsForOrder(orderId: String): List<Payment> = withContext(Dispatchers.IO) {
        return@withContext paymentRepository.findByOrderId(orderId)
    }

    /*
     * Retrieve all payments
     * @return A list of all payments
     */
    override suspend fun listAllPayments(): List<Payment> = withContext(Dispatchers.IO) {
        return@withContext paymentRepository.findAll()
    }
}
