package presentation.cli

import domain.models.Customer
import domain.services.CustomerService
import java.util.*

class CustomerView(
    private val customerService: CustomerService,
    private val scanner: Scanner = Scanner(System.`in`)
) {
    
    fun showCustomerMenu() {
        while (true) {
            println("\n=== Customer Management ===")
            println("1. Add New Customer")
            println("2. List All Customers")
            println("3. View Customer Details")
            println("4. Update Customer")
            println("5. Delete Customer")
            println("6. Back to Main Menu")
            print("\nChoose an option (1-5): ")

            when (readIntInRange(1..5)) {
                1 -> addNewCustomer()
                2 -> listCustomers()
                3 -> viewCustomerDetails()
                4 -> updateCustomer()
                5 -> deleteCustomer()
                6 -> println("\nReturning to main menu...")
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun addNewCustomer() {
        println("\n=== Add New Customer ===")
        print("Name: ")
        val name = scanner.nextLine().trim()
        print("Phone: ")
        val phone = scanner.nextLine().trim()
        print("Email (optional): ")
        val email = scanner.nextLine().trim()
        print("Address (optional): ")
        val address = scanner.nextLine().trim()
        print("Notes (optional): ")
        val notes = scanner.nextLine().trim()

        if (customerService.addCustomer(name, phone)) {
            println("\nCustomer added successfully!")
        } else {
            println("\nFailed to add customer.")
        }
    }

    private fun listCustomers() {
        val customers = customerService.listAllCustomers()
        
        if (customers.isEmpty()) {
            println("\nNo customers found.")
            return
        }

        println("\n=== Customer List ===")
        println("ID\tName\t\tPhone\t\tStatus")
        println("-".repeat(50))
        
        customers.forEach { customer ->
            val status = if (customer.isActive) "Active" else "Inactive"
            println("${customer.id.take(8)}...\t${customer.name.padEnd(15)}\t${customer.phone}\t$status")
        }
        
        println("\nTotal: ${customers.size} customers")
    }

    private fun viewCustomerDetails() {
        val customer = findCustomer("Enter customer ID or phone number: ") ?: return
        
        println("\n=== Customer Details ===")
        println("ID: ${customer.id}")
        println("Name: ${customer.name}")
        println("Phone: ${customer.phone}")
        customer.email?.let { println("Email: $it") }
        customer.address?.let { println("Address: $it") }
        customer.notes?.let { println("Notes: $it") }
        println("Status: ${if (customer.isActive) "Active" else "Inactive"}")
    }

    private fun updateCustomer() {
        val customer = findCustomer("Enter customer ID or phone number to update: ") ?: return
        
        println("\nLeave field blank to keep current value")
        
        print("Name [${customer.name}]: ")
        val name = scanner.nextLine().trim().takeIf { it.isNotBlank() } ?: customer.name
        
        print("Phone [${customer.phone}]: ")
        val phone = scanner.nextLine().trim().takeIf { it.isNotBlank() } ?: customer.phone
        
        print("Email [${customer.email ?: "Not set"}]: ")
        val email = scanner.nextLine().trim().takeIf { it.isNotBlank() } ?: customer.email
        
        print("Address [${customer.address ?: "Not set"}]: ")
        val address = scanner.nextLine().trim().takeIf { it.isNotBlank() } ?: customer.address
        
        print("Notes [${customer.notes ?: "Not set"}]: ")
        val notes = scanner.nextLine().trim().takeIf { it.isNotBlank() } ?: customer.notes
        
        val updatedCustomer = customer.copy(
            name = name,
            phone = phone,
            email = email,
            address = address,
            notes = notes
        )
        
        if (customerService.updateCustomer(updatedCustomer.id, name, phone)) {
            println("\nCustomer updated successfully!")
        } else {
            println("\nFailed to update customer.")
        }
    }

    private fun deleteCustomer() {
        val customer = findCustomer("Enter customer ID or phone number to delete: ") ?: return
        
        print("Are you sure you want to delete ${customer.name}? (yes/no): ")
        val confirmation = scanner.nextLine().trim().lowercase()
        
        if (confirmation == "yes" || confirmation == "y") {
            if (customerService.deleteCustomer(customer.id)) {
                println("\nCustomer deleted successfully!")
            } else {
                println("\nFailed to delete customer.")
            }
        } else {
            println("\nOperation cancelled.")
        }
    }

    private fun findCustomer(prompt: String): Customer? {
        while (true) {
            print("\n$prompt")
            val input = scanner.nextLine().trim()
            
            if (input.isBlank()) {
                println("Operation cancelled.")
                return null
            }
            
            // Try to find by ID first
            val customer = customerService.getCustomerById(input) 
                ?: customerService.getCustomerByPhone(input)
            
            if (customer != null) return customer
            
            println("Customer not found. Try again or press Enter to cancel.")
        }
    }
    
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            try {
                val input = scanner.nextLine().trim().toInt()
                if (input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                print("Invalid input. Please enter a number: ")
            }
        }
    }
}