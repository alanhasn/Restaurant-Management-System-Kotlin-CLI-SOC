package presentation.cli

// Dependencies
import domain.models.Table
import domain.models.utils.TableStatus
import domain.services.TableService
import kotlinx.coroutines.runBlocking
import java.util.UUID

/**
 * TableView handles all table-related user interactions.
 * It communicates with the TableService to perform operations.
 */
class TableView(private val tableService: TableService) {

    /**
     * Displays the table management menu and handles user input.
     */
    fun showTableMenu() {
        while (true) {
            println("\n=== Table Management ===")
            println("1. Add New Table")
            println("2. View All Tables")
            println("3. Update Table Information")
            println("4. Delete Table")
            println("5. Update Table Status")
            println("6. Back to Main Menu")
            print("\nChoose an option (1-6): ")

            when (readIntInRange(1..6)) {
                1 -> addTable()
                2 -> listAllTables()
                3 -> updateTable()
                4 -> deleteTable()
                5 -> selectStatus()
                6 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * Adds a new table.
     */
    private fun addTable() = runBlocking {
        println("\n=== Add New Table ===")
        val tableNumber = readInt("Table Number: ")
        val capacity = readInt("Capacity: ")
        val location = readOptionalInput("Location (optional): ")
        val description = readOptionalInput("Description (optional): ")

        try {
            val table = Table(UUID.randomUUID().toString() ,tableNumber, capacity, TableStatus.AVAILABLE , location, description)
            val success = tableService.addTable(table)
            if (success) {
                println("\nTable added successfully!")
            } else {
                println("\nFailed to add table.")
            }
        } catch (e: Exception) {
            println("\nError adding table: ${e.message}")
        }
    }

    /**
     * Lists all tables.
     */
    private fun listAllTables() = runBlocking {
        try {
            val tables = tableService.listAllTables()
            if (tables.isEmpty()) {
                println("\nNo tables found.")
                return@runBlocking
            }

            println("\n=== Table List ===")
            println("-".repeat(40))

            tables.forEachIndexed { index, table ->
                println("Table #${index + 1}")
                println("ID       : ${table.id}")
                println("Number   : ${table.tableNumber}")
                println("Capacity : ${table.capacity}")
                println("Status   : ${table.status}")
                println("-".repeat(40))
            }

            println("Total tables: ${tables.size}")
        } catch (e: Exception) {
            println("\nError fetching tables: ${e.message}")
        }
    }

    /**
     * Updates the information of a table.
     */
    private fun updateTable() = runBlocking {
        listAllTables()
        val table = findTable("Enter table ID to update: ") ?: return@runBlocking

        println("\nUpdating information for table ${table.tableNumber}. Press Enter to keep current value.")

        val tableNumber = readOptionalInput("Table Number [${table.tableNumber}]: ")?.toIntOrNull()
        val capacity = readOptionalInput("Capacity [${table.capacity}]: ")?.toIntOrNull()
        val location = readOptionalInput("Location [${table.location ?: "N/A"}]: ")
        val description = readOptionalInput("Description [${table.description ?: "N/A"}]: ")

        try {
            val updatedTable = table.copy(
                tableNumber = tableNumber ?: table.tableNumber,
                capacity = capacity ?: table.capacity,
                location = location ?: table.location,
                description = description ?: table.description
            )
            val success = tableService.updateTable(table.id ,updatedTable )
            if (success) {
                println("\nTable information updated successfully!")
            } else {
                println("\nFailed to update table information.")
            }
        } catch (e: Exception) {
            println("\nError updating table: ${e.message}")
        }
    }

    /**
     * Deletes a table.
     */
    private fun deleteTable() = runBlocking {
        listAllTables()
        val table = findTable("Enter table ID to delete: ") ?: return@runBlocking

        print("\nAre you sure you want to delete table ${table.tableNumber}? (yes/no): ")
        if (readLine()?.trim()?.equals("yes", ignoreCase = true) == true) {
            try {
                val success = tableService.deleteTable(table.id)
                if (success) {
                    println("\nTable deleted successfully!")
                } else {
                    println("\nFailed to delete table.")
                }
            } catch (e: Exception) {
                println("\nError deleting table: ${e.message}")
            }
        } else {
            println("\nOperation cancelled.")
        }
    }

    /**
     * Finds a table by ID.
     */
    private suspend fun findTable(prompt: String): Table? {
        while (true) {
            print("\n$prompt")
            val tableId = readLine()?.trim() ?: ""
            if (tableId.isBlank()) {
                println("Operation cancelled.")
                return null
            }
            try {
                val table = tableService.getTableById(tableId)
                if (table != null) return table
                println("Table not found. Try again or press Enter to cancel.")
            } catch (e: Exception) {
                println("Error finding table: ${e.message}")
                return null
            }
        }

    }

    /**
     * Selects a table status.
     */
    private fun selectStatus(): TableStatus {
        println("Select a status:")
        val statuses = TableStatus.entries.toTypedArray()
        statuses.forEachIndexed { index, status ->
            println("${index + 1}. ${status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }}")
        }

        while (true) {
            print("Choose a status (1-${statuses.size}): ")
            val choice = readIntInRange(1..statuses.size)
            val selectedStatus = statuses[choice - 1]

            println("\nTable status updated to: ${selectedStatus.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }}")
            return selectedStatus
        }
    }


    /**
     * Reads an optional input from the user.
     */
    private fun readOptionalInput(prompt: String): String? {
        print(prompt)
        return readLine()?.trim()?.takeIf { it.isNotBlank() }
    }

    /**
     * Reads an integer input from the user.
     */
    private fun readInt(prompt: String): Int {
        while (true) {
            print(prompt)
            try {
                return readLine()?.trim()?.toInt() ?: continue
            } catch (e: NumberFormatException) {
                println("Invalid input. Please enter a valid number.")
            }
        }
    }

    /**
     * Reads an integer input from the user within a specified range.
     */
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