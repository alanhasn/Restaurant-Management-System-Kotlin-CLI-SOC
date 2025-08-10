package data.repository

// Dependencies
import domain.models.Payment
import domain.models.utils.PaymentStatus
import java.util.UUID // For payment ID generation


// In-Memory Payment Repository (Implementation of PaymentRepository interface)
class InMemoryPaymentRepository: PaymentRepository{

    // In-memory map to store payments
    private val payments = mutableMapOf<String , Payment>()

    /*
     * Saves a new payment to the repository.
     * @param payment The payment to be saved.
     * @return true if the payment was saved successfully, false otherwise.
     */
    override fun save(payment: Payment): Boolean {
        val id = payment.id.ifBlank { UUID.randomUUID().toString() }
        val paymentWithId = payment.copy(id=id)
        payments[id] = paymentWithId
        return true
    }

    /*
     * Finds a payment by its ID.
     * @param id The ID of the payment to find.
     * @return The payment if found, null otherwise.
     */
    override fun findById(id: String): Payment? {
        return payments[id]
    }

    /*
     * Finds payments by order ID.
     * @param orderId The ID of the order to find payments for.
     * @return A list of payments associated with the order.
     */
    override fun findByOrderId(orderId: String): List<Payment> {
        return payments.values.filter { it.orderId == orderId }
    }

    /*
     * Finds payments by their status.
     * @param status The status of the payments to find.
     * @return A list of payments with the specified status.
     */
    override fun findByStatus(status: PaymentStatus): List<Payment> {
        return payments.values.filter { it.status == status }
    }

    /*
     * Retrieves all payments from the repository.
     * @return A list of all payments.
     */
    override fun findAll(): List<Payment> {
        return payments.values.toList()
    }

    /*
     * Updates a payment in the repository.
     * @param payment The payment to update.
     * @return The updated payment if successful, null otherwise.
     */
    override fun update(payment: Payment): Result<Payment> {
        val id = payment.id
        if (!payments.containsKey(id)){
            return Result.failure(IllegalArgumentException("No Payment found with id: $id"))
        }
        payments[id] = payment
        return Result.success(payment)
    }

    /*
     * Deletes a payment by its ID.
     * @param id The ID of the payment to delete.
     * @return true if the payment was deleted successfully, false otherwise.
     */
    override fun delete(id: String): Boolean {
        return payments.remove(id) != null
    }
}