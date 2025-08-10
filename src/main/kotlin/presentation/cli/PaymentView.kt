package presentation.cli

// Dependencies
import domain.models.Payment
import domain.models.utils.PaymentMethod
import domain.services.PaymentService
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.format.DateTimeFormatter // For LocalDate

/**
 * PaymentView handles all payment-related user interactions.
 * It communicates with the PaymentService to perform operations.
 */
class PaymentView(
    private val paymentService: PaymentService,
    private val orderView: OrderView
) {
    // Formatter for date and time
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * Displays the payment management menu and handles user input.
     */
    fun showPaymentMenu() {
        while (true) {
            println("\n=== Payment Management ===")
            println("1. Make a Payment")
            println("2. View Payment Details")
            println("3. View Payments for an Order")
            println("4. List All Payments")
            println("5. Back to Main Menu")
            print("\nChoose an option (1-5): ")

            when (readIntInRange(1..5)) {
                1 -> makePayment()
                2 -> viewPaymentDetails()
                3 -> viewPaymentsForOrder()
                4 -> listAllPayments()
                5 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * Processes a new payment for a given order.
     */
    private fun makePayment() = runBlocking {
        println("\n=== Make a Payment ===")
        orderView.showOrders()
        val orderId = readNonEmptyInput("Enter Order ID: ", "Order ID cannot be empty.")

        val amount = readBigDecimal("Enter amount to pay: $")

        val paymentMethod = selectPaymentMethod()

        try {
            val success = paymentService.makePayment(orderId, amount, paymentMethod)
            if (success) {
                println("\nPayment successful!")
            } else {
                println("\nPayment failed. Please check the order details and try again.")
            }
        } catch (e: Exception) {
            println("\nError processing payment: ${e.message}")
        }
    }

    /**
     * Displays details for a specific payment.
     */
    private fun viewPaymentDetails() = runBlocking {
        println("\n=== View Payment Details ===")
        listAllPayments()
        val paymentId = readNonEmptyInput("Enter Payment ID: ", "Payment ID cannot be empty.")

        try {
            val payment = paymentService.getPaymentById(paymentId)
            if (payment == null) {
                println("\nPayment with ID '$paymentId' not found.")
                return@runBlocking
            }
            printPaymentDetails(payment)
        } catch (e: Exception) {
            println("\nError retrieving payment details: ${e.message}")
        }
    }

    /**
     * Lists all payments made for a specific order.
     */
    private fun viewPaymentsForOrder() = runBlocking {
        println("\n=== View Payments for an Order ===")
        orderView.showOrders()
        val orderId = readNonEmptyInput("Enter Order ID: ", "Order ID cannot be empty.")

        try {
            val payments = paymentService.getPaymentsForOrder(orderId)
            if (payments.isEmpty()) {
                println("\nNo payments found for order '$orderId'.")
                return@runBlocking
            }

            println("\n--- Payments for Order: $orderId ---")
            println("-".repeat(40))

            payments.forEachIndexed { index, payment ->
                val amount = "$${payment.amount.setScale(2)}"
                val method = payment.paymentMethod.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
                val date = payment.paymentDate.format(dateTimeFormatter)

                println("Payment #${index + 1}")
                println("ID     : ${payment.id}")
                println("Amount : $amount")
                println("Method : $method")
                println("Date   : $date")
                println("-".repeat(40))
            }

            println("Total payments: ${payments.size}")
        } catch (e: Exception) {
            println("\nError retrieving payments: ${e.message}")
        }
    }

    /**
     * Lists all payments made.
     */
    private fun listAllPayments() = runBlocking {
        try {
            val payments = paymentService.listAllPayments()
            if (payments.isEmpty()) {
                println("\nNo payments found.")
                return@runBlocking
            }

            println("\n=== Payment List ===")
            println("-".repeat(40))

            payments.forEachIndexed { index, payment ->
                val amount = "$${payment.amount.setScale(2)}"

                println("Payment #${index + 1}")
                println("ID         : ${payment.id}")
                println("Order ID   : ${payment.orderId}")
                println("Amount     : $amount")
                println("Method     : ${payment.paymentMethod}")
                println("Status     : ${payment.status}")
                println("-".repeat(40))
            }

            println("Total payments: ${payments.size}")
        } catch (e: Exception) {
            println("\nError listing payments: ${e.message}")
        }
    }

    /**
     * Helper function to print payment details.
     */
    private fun printPaymentDetails(payment: Payment, simple: Boolean = false) {
        if (simple) {
            // Simplified view
                println("  - ID: ${payment.id}, Amount: $${payment.amount.setScale(2)}, Method: ${payment.paymentMethod}, Date: ${payment.paymentDate.format(dateTimeFormatter)}")
        } else {
            println("\n--- Payment Details ---")
            println("Payment ID: ${payment.id}")
            println("Order ID: ${payment.orderId}")
            println("Amount Paid: $${payment.amount.setScale(2)}")
            println("Payment Method: ${payment.paymentMethod}")
            println("Payment Date: ${payment.paymentDate.format(dateTimeFormatter)}")
            println("-".repeat(25))
        }
    }

    /**
     * Displays a menu of payment methods and allows the user to select one.
     */
    private fun selectPaymentMethod(): PaymentMethod {
        println("\nSelect a payment method:")
        PaymentMethod.entries.forEachIndexed { index, method ->
            println("${index + 1}. ${method.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }}")
        }

        print("Enter method number (1-${PaymentMethod.entries.size}): ")

        return try {
            val methodIndex = readIntInRange(1..PaymentMethod.entries.size) - 1
            PaymentMethod.entries[methodIndex]
        } catch (e: Exception) {
            println(" Invalid selection. Using default: ${PaymentMethod.CASH}")
            PaymentMethod.CASH
        }
    }

    /**
     * Helper function to read a non-empty input from the user.
     */
    private fun readNonEmptyInput(prompt: String, errorMessage: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (!input.isNullOrBlank()) {
                return input
            }
            println(errorMessage)
        }
    }

    /**
     * Helper function to read a BigDecimal value from user input.
     */
    private fun readBigDecimal(prompt: String): BigDecimal {
        while (true) {
            print(prompt)
            try {
                return readLine()?.trim()?.toBigDecimal() ?: continue
            } catch (e: NumberFormatException) {
                println("Invalid number format. Please try again.")
            }
        }
    }

    /**
     * Helper function to read an integer within a specified range from user input.
     */
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            try {
                val input = readLine()?.trim()?.toInt()
                    ?: throw NumberFormatException("Input cannot be empty")
                if (input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                print("Invalid input. Please enter a valid number: ")
            }
        }
    }
}