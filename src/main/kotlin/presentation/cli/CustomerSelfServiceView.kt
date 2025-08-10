package presentation.cli

import domain.models.Customer
import domain.models.User
import domain.services.CustomerService
import domain.services.OrderService
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

/**
 * CustomerSelfServiceView provides a dedicated interface for customers to manage their own profile and view orders.
 */
class CustomerSelfServiceView(
    private val customerService: CustomerService,
    private val orderService: OrderService,
    private val currentUser: User
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * Displays the customer self-service menu and handles user input.
     */
    fun showSelfServiceMenu() {
        while (true) {
            println("\n=== Customer Self-Service ===")
            println("1. View My Profile")
            println("2. Update My Profile")
            println("3. View My Order History")
            println("4. Back to Main Menu")
            print("\nChoose an option (1-4): ")

            when (readIntInRange(1..4)) {
                1 -> viewMyProfile()
                2 -> updateMyProfile()
                3 -> viewMyOrderHistory()
                4 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * Fetches and displays the current customer's profile.
     */
    private fun viewMyProfile() = runBlocking {
        val customer = getCustomerProfile()
        if (customer != null) {
            printCustomerDetails(customer)
        } else {
            println("\nCould not find your customer profile. Please contact support.")
        }
    }

    /**
     * Updates the current customer's profile information.
     */
    private fun updateMyProfile() = runBlocking {
        val customer = getCustomerProfile() ?: run {
            println("\nCould not find your customer profile to update.")
            return@runBlocking
        }

        println("\nUpdating your profile. Press Enter to keep the current value.")

        val name = readOptionalInput("Name [${customer.name}]: ")
        val phone = readOptionalInput("Phone [${customer.phone}]: ")
        val email = readOptionalInput("Email [${customer.email ?: "N/A"}]: ")
        val address = readOptionalInput("Address [${customer.address ?: "N/A"}]: ")

        if (name == null && phone == null && email == null && address == null) {
            println("\nNo changes were made.")
            return@runBlocking
        }

        try {
            val success = customerService.updateCustomer(currentUser.id, name, phone, email, address, null)
            if (success) {
                println("\nYour profile has been updated successfully!")
            } else {
                println("\nFailed to update your profile.")
            }
        } catch (e: Exception) {
            println("\nAn error occurred while updating your profile: ${e.message}")
        }
    }

    /**
     * Fetches and displays the order history for the current customer.
     */
    private fun viewMyOrderHistory() = runBlocking {
        try {
            val orders = orderService.getOrdersByCustomer(currentUser.id)
            if (orders.isEmpty()) {
                println("\nℹ️ You have no past orders.")
                return@runBlocking
            }

            println("\n=== Your Order History ===")
            orders.forEach { order ->
                println("\n--- Order #${order.id} ---")
                println("Status: ${order.status}")
                println("Placed on: ${order.orderTime.format(dateFormatter)}")
                println("Total: $${order.totalAmount}")
                if (order.orderItems.isNotEmpty()) {
                    println("Items:")
                    order.orderItems.forEach { orderItem ->
                        println("  - ItemID: ${orderItem.menuItemId}, Quantity: ${orderItem.quantity}, Price: ${orderItem.unitPrice}")
                    }
                }
                println("-".repeat(25))
            }
        } catch (e: Exception) {
            println("\nAn error occurred while fetching your order history: ${e.message}")
        }
    }

    /**
     * Helper function to get the customer profile associated with the current user.
     */
    private suspend fun getCustomerProfile(): Customer? {
        return try {
            customerService.getCustomerById(currentUser.id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Prints the details of a customer's profile.
     */
    private fun printCustomerDetails(customer: Customer) {
        println("\n--- Your Profile ---")
        println("Name: ${customer.name}")
        println("Email: ${customer.email ?: "N/A"}")
        println("Phone: ${customer.phone}")
        println("Address: ${customer.address ?: "N/A"}")
        println("-".repeat(25))
    }

    // Input helper functions

    private fun readOptionalInput(prompt: String): String? {
        print(prompt)
        return readLine()?.trim()?.takeIf { it.isNotBlank() }
    }

    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            print("Choose an option: ")
            try {
                val input = readLine()?.trim()?.toInt() ?: throw NumberFormatException()
                if (input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                print("Invalid input. Please enter a valid number: ")
            }
        }
    }
}