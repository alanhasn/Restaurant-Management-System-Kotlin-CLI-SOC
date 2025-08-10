package presentation.cli

import domain.models.Employee
import domain.services.EmployeeService
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter // For LocalDate
import java.time.format.DateTimeParseException // For LocalDate parsing

/**
 * EmployeeView handles all employee-related user interactions.
 * It communicates with the EmployeeService to perform operations.
 */
class EmployeeView(private val employeeService: EmployeeService) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Displays the employee management menu and handles user input.
     */
    fun showEmployeeMenu() {
        while (true) {
            println("\n=== Employee Management ===")
            println("1. Hire New Employee")
            println("2. View All Employees")
            println("3. View Employee Details")
            println("4. Update Employee Information")
            println("5. Fire Employee")
            println("6. Back to Main Menu")
            print("\nChoose an option (1-6): ")

            when (readIntInRange(1..6)) {
                1 -> hireEmployee()
                2 -> listAllEmployees()
                3 -> viewEmployeeDetails()
                4 -> updateEmployee()
                5 -> fireEmployee()
                6 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * Gathers information and hires a new employee.
     */
    private fun hireEmployee() = runBlocking {
        println("\n=== Hire New Employee ===")
        val name = readNonEmptyInput("Full Name: ", "Name cannot be empty.")
        val position = readNonEmptyInput("Position: ", "Position cannot be empty.")
        val salary = readDouble("Salary: ")
        val contactNumber = readNonEmptyInput("Contact Number: ", "Contact number cannot be empty.")
        val email = readNonEmptyInput("Email: ", "Email cannot be empty.")
        val address = readNonEmptyInput("Address: ", "Address cannot be empty.")
        val hireDate = readDate("Hire Date (YYYY-MM-DD): ", defaultToday = true)
        val userId = readNonEmptyInput("Associated User ID: ", "User ID cannot be empty.")

        // Optional fields
        val emergencyContact = readOptionalInput("Emergency Contact (optional): ")
        val dateOfBirth = readOptionalInput("Date of Birth (YYYY-MM-DD optional):")
        val idNumber = readOptionalInput("Identification Number (optional): ")

        try {
            val success = employeeService.hireEmployee(
                name, position, hireDate, salary, contactNumber, email, address,
                emergencyContact, dateOfBirth, idNumber, userId
            )
            if (success) {
                println("\nEmployee hired successfully!")
            } else {
                println("\nFailed to hire employee.")
            }
        } catch (e: Exception) {
            println("\nError hiring employee: ${e.message}")
        }
    }

    /**
     * Lists all employees with basic information.
     */
    fun listAllEmployees() = runBlocking {
        try {
            val employees = employeeService.listAllEmployees()
            if (employees.isEmpty()) {
                println("\nNo employees found.")
                return@runBlocking
            }

            println("\n=== Employee List ===")
            println("-".repeat(40))
            employees.forEachIndexed { index, employee ->
                val status = if (employee.isActive) "Active" else "Inactive"
                println("Employee #${index + 1}")
                println("ID      : ${employee.id}")
                println("Name    : ${employee.name}")
                println("Position: ${employee.position}")
                println("Status  : $status")
                println("-".repeat(40))
            }
            println("Total employees: ${employees.size}")
        } catch (e: Exception) {
            println("\nError fetching employees: ${e.message}")
        }
    }

    /**
     * Displays detailed information for a single employee.
     */
    private fun viewEmployeeDetails() = runBlocking {
        listAllEmployees()
        val employee = findEmployee("Enter employee ID to view details: ") ?: return@runBlocking
        printEmployeeDetails(employee)
    }

    /**
     * Updates an existing employee's information.
     */
    private fun updateEmployee() = runBlocking {
        listAllEmployees()
        val employee = findEmployee("Enter employee ID to update: ") ?: return@runBlocking

        println("\nUpdating information for ${employee.name}. Press Enter to keep current value.")

        val name = readOptionalInput("Name [${employee.name}]: ")
        val position = readOptionalInput("Position [${employee.position}]: ")
        val salary = readOptionalInput("Salary [${employee.salary}]: ")?.toDoubleOrNull()
        val contactNumber = readOptionalInput("Contact Number [${employee.contactNumber}]: ")
        val email = readOptionalInput("Email [${employee.email}]: ")
        val address = readOptionalInput("Address [${employee.address}]: ")
        val emergencyContact = readOptionalInput("Emergency Contact [${employee.emergencyContact ?: "N/A"}]: ")
        val isActive = readOptionalInput("Is Active (true/false) [${employee.isActive}]: ")?.toBoolean()

        try {
            val success = employeeService.updateEmployee(
                employee.id, name, position, salary, contactNumber, email, address, emergencyContact, isActive
            )
            if (success) {
                println("\nEmployee information updated successfully!")
            } else {
                println("\nFailed to update employee information.")
            }
        } catch (e: Exception) {
            println("\nError updating employee: ${e.message}")
        }
    }

    /**
     * Fires an employee, marking them as inactive.
     */
    private fun fireEmployee() = runBlocking {
        listAllEmployees()
        val employee = findEmployee("Enter employee ID to fire: ") ?: return@runBlocking

        if (!employee.isActive) {
            println("\nThis employee is already inactive.")
            return@runBlocking
        }

        print("\nAre you sure you want to fire ${employee.name}? (yes/no): ")
        if (readLine()?.trim()?.equals("yes", ignoreCase = true) == true) {
            try {
                val success = employeeService.fireEmployee(employee.id)
                if (success) {
                    println("\nEmployee has been fired.")
                } else {
                    println("\nFailed to fire employee.")
                }
            } catch (e: Exception) {
                println("\nError firing employee: ${e.message}")
            }
        } else {
            println("\nOperation cancelled.")
        }
    }

    /**
     * Helper function to find an employee by ID.
     */
    private suspend fun findEmployee(prompt: String): Employee? {
        while (true) {
            print("\n$prompt")
            val employeeId = readLine()?.trim() ?: ""
            if (employeeId.isBlank()) {
                println("Operation cancelled.")
                return null
            }
            try {
                val employee = employeeService.getEmployeeById(employeeId)
                if (employee != null) return employee
                println("Employee not found. Try again or press Enter to cancel.")
            } catch (e: Exception) {
                println("Error finding employee: ${e.message}")
                return null
            }
        }
    }


    /**
     * Helper function to print detailed information about an employee.
     */
    private fun printEmployeeDetails(employee: Employee) {
        println("\n=== Employee Details ===")
        println("ID                : ${employee.id}")
        println("Name              : ${employee.name}")
        println("Position          : ${employee.position}")
        println("Salary            : $${"%.2f".format(employee.salary)}")
        println("Contact Number    : ${employee.contactNumber}")
        println("Email             : ${employee.email}")
        println("Address           : ${employee.address}")
        println("Hired On          : ${employee.hireDate.format(dateFormatter)}")
        println("Status            : ${if (employee.isActive) "Active" else "Inactive"}")

        employee.emergencyContact?.let {
            println("Emergency Contact : $it")
        }
        employee.dateOfBirth?.let {
            println("Date of Birth     : ${it.format(dateFormatter)}")
        }
        employee.identificationNumber?.let {
            println("ID Number         : $it")
        }

        println("-".repeat(40))
    }

    // Input helper functions
    private fun readNonEmptyInput(prompt: String, errorMessage: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (!input.isNullOrBlank()) return input
            println(errorMessage)
        }
    }

    // Helper function to read an optional input from the user
    private fun readOptionalInput(prompt: String): String? {
        print(prompt)
        return readLine()?.trim()?.takeIf { it.isNotBlank() }
    }

    // Helper function to read an integer within a specified range from user input
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            try {
                val input = readLine()?.trim()?.toInt() ?: throw NumberFormatException()
                if (input in range) return input
                print("Please enter a number between ${range.first} and ${range.last}: ")
            } catch (e: NumberFormatException) {
                print(" Invalid input. Please enter a valid number: ")
            }
        }
    }

    // Helper function to read a double from user input
    private fun readDouble(prompt: String): Double {
        while (true) {
            try {
                print(prompt)
                return readLine()?.trim()?.toDouble() ?: throw NumberFormatException()
            } catch (e: NumberFormatException) {
                print(" Invalid input. Please enter a valid number: ")
            }
        }
    }

    // Helper function to read a date from user input
    private fun readDate(prompt: String, defaultToday: Boolean): LocalDate {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (input.isNullOrBlank()) {
                if (defaultToday) return LocalDate.now()
                println("Date is required.")
                continue
            }
            try {
                return LocalDate.parse(input, dateFormatter)
            } catch (e: DateTimeParseException) {
                print("Invalid date format. Please use YYYY-MM-DD: ")
            }
        }
    }

}