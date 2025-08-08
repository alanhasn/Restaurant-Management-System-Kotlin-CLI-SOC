package presentation.cli

import java.util.*

class AdminView(
    private val scanner: Scanner = Scanner(System.`in`)
) {

    fun showAdminMenu() {
        while (true) {
            println("\n=== Main Menu (Admin) ===\n")
            println("1. Manage Menu")
            println("2. Manage Orders")
            println("3. Manage Tables")
            println("4. Manage Staff")
            println("5. View Reports")
            println("6. Logout")

            print("\nPlease enter your choice (1-6): ")

            when (readIntInRange(1..6)) {
                1 -> manageMenu()
                2 -> manageOrders()
                3 -> manageTables()
                4 -> manageStaff()
                5 -> viewReports()
                6 -> {
                    println("\nLogging out...")
                    return
                }
            }
        }
    }

    private fun manageMenu() {
        println("\n=== Manage Menu ===")
        println("This feature is under construction.")
        println("Press Enter to return...")
        scanner.nextLine()
    }

    private fun manageOrders() {
        println("\n=== Manage Orders ===")
        println("This feature is under construction.")
        println("Press Enter to return...")
        scanner.nextLine()
    }

    private fun manageTables() {
        println("\n=== Manage Tables ===")
        println("This feature is under construction.")
        println("Press Enter to return...")
        scanner.nextLine()
    }

    private fun manageStaff() {
        println("\n=== Manage Staff ===")
        println("This feature is under construction.")
        println("Press Enter to return...")
        scanner.nextLine()
    }

    private fun viewReports() {
        println("\n=== View Reports ===")
        println("This feature is under construction.")
        println("Press Enter to return...")
        scanner.nextLine()
    }

    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            try {
                val input = scanner.nextLine().trim()
                if (input.equals("exit", ignoreCase = true)) return 6 // Quick exit to logout

                val choice = input.toInt()
                if (choice in range) return choice
                print("Please enter a number between ${range.first} and ${range.last}: ")
            } catch (e: NumberFormatException) {
                print("Invalid input. Please enter a number: ")
            }
        }
    }
}
