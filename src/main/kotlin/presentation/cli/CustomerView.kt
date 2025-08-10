package presentation.cli

// Dependencies
import domain.models.Customer
import domain.services.CustomerService
import kotlinx.coroutines.runBlocking

class CustomerView(
    private val customerService: CustomerService
) {

    /**
     * This function displays the main menu for customer management.
     * It provides options to add, list, view, update, and delete customers.
     * It also handles user input and calls the corresponding functions to perform actions.
     */
    fun showCustomerMenu() {
        while (true) {
            println("\n=== Customer Management ===")
            println("1. Add New Customer")
            println("2. List All Customers")
            println("3. View Customer Details")
            println("4. Update Customer")
            println("5. Delete Customer")
            println("6. Back to Main Menu")
            print("\nChoose an option (1-6): ")

            when (readIntInRange(1..6)) {
                1 -> addNewCustomer()
                2 -> listCustomers()
                3 -> viewCustomerDetails()
                4 -> updateCustomer()
                5 -> deleteCustomer()
                6 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * This function adds a new customer.
     * It prompts the user to enter the customer's details, such as name, phone number, email, address, and notes.
     * It then calls the addCustomer function in the customerService to add the new customer.
     */
    private fun addNewCustomer() = runBlocking {
        println("\n=== Add New Customer ===")

        val name = readNonEmptyInput("Name: ", "Name cannot be empty")
        val phone = readNonEmptyInput("Phone: ", "Phone number cannot be empty")
        val email = readOptionalInput("Email (optional): ")
        val address = readOptionalInput("Address (optional): ")
        val notes = readOptionalInput("Notes (optional): ")

        try {
            val success = customerService.addCustomer(name, phone, email, address, notes)
            println(if (success) "\nCustomer added successfully!"
            else "\nFailed to add customer. The phone number might already exist.")
        } catch (e: Exception) {
            println("\nError: ${e.message}")
        }
    }

    /**
     * This function lists all customers.
     * It calls the listAllCustomers function in the customerService to retrieve a list of all customers.
     * If no customers are found, it prints a message indicating that no customers were found.
     * Otherwise, it prints the list of customers, including their ID, name, phone number, and status.
     */
    fun listCustomers() = runBlocking {
        try {
            val customers = customerService.listAllCustomers()

            if (customers.isEmpty()) {
                println("\nNo customers found.")
                return@runBlocking
            }

            println("\n=== Customer List ===")
            println("-".repeat(40))
            customers.forEachIndexed { index, customer ->
                val status = if (customer.isActive) "Active" else "Inactive"
                println("Customer #${index + 1}")
                println("ID     : ${customer.id}")
                println("Name   : ${customer.name}")
                println("Phone  : ${customer.phone}")
                println("Status : $status")
                println("-".repeat(40))
            }
            println("Total customers: ${customers.size}")
        } catch (e: Exception) {
            println("\nError fetching customers: ${e.message}")
        }
    }

    /**
     * This function displays the details of a specific customer.
     * It prompts the user to enter the customer's ID or phone number, and then calls the getCustomerById or getCustomerByPhone function in the customerService to retrieve the customer details.
     * If no customer is found, it prints a message indicating that the customer was not found.
     * Otherwise, it prints the customer details, including their ID, name, phone number, email, address, notes, and status.
     */
    private fun viewCustomerDetails() = runBlocking {
        listCustomers()
        val customer = findCustomer("Enter customer ID or phone number: ") ?: return@runBlocking

        println("\n=== Customer Details ===")
        println("ID      : ${customer.id}")
        println("Name    : ${customer.name}")
        println("Phone   : ${customer.phone}")
        customer.email?.let { println("Email   : $it") }
        customer.address?.let { println("Address : $it") }
        customer.notes?.let { println("Notes   : $it") }
        println("Status  : ${if (customer.isActive) "Active" else "Inactive"}")
        println("-".repeat(40))
    }

    /**
     * This function updates a customer.
     * It prompts the user to enter the customer's ID or phone number, and then calls the getCustomerById or getCustomerByPhone function in the customerService to retrieve the customer details.
     * If no customer is found, it prints a message indicating that the customer was not found.
     * Otherwise, it prompts the user to enter the new details for the customer, such as name, phone number, email, address, and notes.
     * It then calls the updateCustomer function in the customerService to update the customer's details.
     * If the update is successful, it prints a message indicating that the customer was updated successfully.
     * Otherwise, it prints a message indicating that the customer was not updated successfully.
     */
    private fun updateCustomer() = runBlocking {
        val customer = findCustomer("Enter customer ID or phone number to update: ") ?: return@runBlocking

        println("\nLeave field blank to keep current value")

        val name = readOptionalInput("Name [${customer.name}]: ")
        val phone = readOptionalInput("Phone [${customer.phone}]: ")
        val email = readOptionalInput("Email [${customer.email ?: "Not set"}]: ", allowBlank = true)
        val address = readOptionalInput("Address [${customer.address ?: "Not set"}]: ", allowBlank = true)
        val notes = readOptionalInput("Notes [${customer.notes ?: "Not set"}]: ", allowBlank = true)

        try {
            val success = customerService.updateCustomer(customer.id, name, phone, email, address, notes)
            println(if (success) "\nCustomer updated successfully!"
            else "\nFailed to update customer. The phone number might already be in use.")
        } catch (e: Exception) {
            println("\nError updating customer: ${e.message}")
        }
    }

    /**
     * This function deletes a customer.
     * It prompts the user to enter the customer's ID or phone number, and then calls the getCustomerById or getCustomerByPhone function in the customerService to retrieve the customer details.
     * If no customer is found, it prints a message indicating that the customer was not found.
     * Otherwise, it prompts the user to confirm the deletion of the customer, and then calls the deleteCustomer function in the customerService to delete the customer.
     * If the deletion is successful, it prints a message indicating that the customer was deleted successfully.
     * Otherwise, it prints a message indicating that the customer was not deleted successfully.
     */
    private fun deleteCustomer() = runBlocking {
        val customer = findCustomer("Enter customer ID or phone number to delete: ") ?: return@runBlocking

        print("Are you sure you want to delete customer '${customer.name}'? (yes/no): ")
        val confirmation = readLine()?.trim()?.lowercase()

        if (confirmation == "yes" || confirmation == "y") {
            try {
                if (customerService.deleteCustomer(customer.id)) {
                    println("\nCustomer deleted successfully!")
                } else {
                    println("\nFailed to delete customer. The customer might have active orders.")
                }
            } catch (e: Exception) {
                println("\nError deleting customer: ${e.message}")
            }
        } else {
            println("\nOperation cancelled.")
        }
    }

    /**
     * findCustomer is now a normal function (not expression-body with runBlocking).
     * It uses runBlocking only for the suspend service calls, avoiding type-inference issues.
     */
    fun findCustomer(prompt: String): Customer? {
        while (true) {
            print("\n$prompt")
            val input = readLine()?.trim() ?: ""

            if (input.isBlank()) {
                println("Operation cancelled.")
                return null
            }

            try {
                val customer = runBlocking {
                    customerService.getCustomerById(input) ?: customerService.getCustomerByPhone(input)
                }

                if (customer != null) return customer

                println("Customer not found. Try again or press Enter to cancel.")
            } catch (e: Exception) {
                println("Error finding customer: ${e.message}")
            }
        }
    }

    // Helper function to read an integer within a specified range
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            try {
                val input = readLine()?.trim()?.toIntOrNull()
                if (input != null && input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                print("Invalid input. Please enter a valid number: ")
            }
        }
    }

    // Helper function to read a non-empty input
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

    // Helper function to read an optional input
    private fun readOptionalInput(prompt: String, allowBlank: Boolean = false): String? {
        print(prompt)
        val input = readLine()?.trim()
        return if (input.isNullOrEmpty()) {
            if (allowBlank) "" else null
        } else {
            input
        }
    }
}
