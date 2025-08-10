package presentation.cli

import domain.models.MenuItem
import domain.models.utils.MenuCategory
import domain.services.MenuService
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal

/**
 * MenuView handles all menu-related user interactions.
 * It communicates with the MenuService to perform operations.
 */
class MenuView(
    private val menuService: MenuService
) {
    /**
     * Displays the menu management interface and handles user input.
     */
    fun showMenu() {
        while (true) {
            println("\n=== Menu Management ===")
            println("1. View Menu")
            println("2. Add Menu Item")
            println("3. Update Menu Item")
            println("4. Delete Menu Item")
            println("5. Back to Main Menu")
            print("\nChoose an option (1-5): ")

            when (readIntInRange(1..5)) {
                1 -> viewMenu()
                2 -> addMenuItem()
                3 -> updateMenuItem()
                4 -> deleteMenuItem()
                5 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * Displays the menu in a categorized format.
     */
    fun viewMenu() = runBlocking {
        try {
            val menuItems = menuService.listAllMenuItems()

            if (menuItems.isEmpty()) {
                println("\nThe menu is currently empty.")
                return@runBlocking
            }

            val itemsByCategory = menuItems.groupBy { it.category }

            MenuCategory.entries.forEach { category ->
                val itemsInCategory = itemsByCategory[category] ?: return@forEach

                println("\n=== ${category.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }} ===")

                itemsInCategory.forEach { item ->
                    println("\nID: ${item.id}")
                    println("Name: ${item.name}")
                    println("Price: $${item.price.setScale(2)}")
                    println("Availability: ${if (item.isAvailable) "Available" else "Unavailable"}")
                    item.calories?.let { println("Calories: $it cal") }
                    println("Prep time: ${item.preparationTime} min")
                    println("Description: ${item.description}")
                    if (item.ingredients.isNotEmpty()) {
                        println("Ingredients: ${item.ingredients.joinToString(", ")}")
                    }
                }
            }

            println("\nTotal items: ${menuItems.size}")
        } catch (e: Exception) {
            println("\nError loading menu: ${e.message}")
        }
    }

    /**
     * Adds a new menu item by collecting user input.
     */
    private fun addMenuItem() = runBlocking {
        println("\n=== Add New Menu Item ===")

        val name = readNonEmptyInput("Item name: ", "Item name is required")
        val description = readNonEmptyInput("Description: ", "Description is required")

        val price = readBigDecimal("Price: $")
        val category = selectCategory()

        val preparationTime = readInt("Preparation time (minutes): ", 15)
        val calories = readIntOptional("Calories (optional, press Enter to skip): ")

        print("Ingredients (comma-separated, optional): ")
        val ingredients = readLine()?.trim()?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()

        try {
            val success = menuService.addMenuItem(
                name = name,
                description = description,
                price = price,
                category = category,
                preparationTime = preparationTime,
                ingredients = ingredients,
                calories = calories
            )

            if (success) {
                println("\n Menu item added successfully!")
            } else {
                println("\n Failed to add menu item.")
            }
        } catch (e: Exception) {
            println("\n Error adding menu item: ${e.message}")
        }
    }

    /**
     * Updates an existing menu item.
     */
    private fun updateMenuItem() = runBlocking {
        println("\n=== Update Menu Item ===")
        viewMenu()
        val item = findMenuItem("Enter menu item ID to update: ") ?: return@runBlocking

        println("\nLeave field blank to keep current value")

        val newName = readOptionalInput("Name [${item.name}]: ")
        val newDescription = readOptionalInput("Description [${item.description}]: ")
        val newPrice = readBigDecimalOptional("Price [${item.price}]: ")
        val newCategory = selectCategoryOptional("Category [${item.category}]: ")
        val newPreparationTime = readIntOptional("Preparation time [${item.preparationTime} min]: ")
        val newCalories = readIntOptional("Calories [${item.calories ?: "Not set"}]: ")
        val newIngredients = readIngredientsOptional("Ingredients [${item.ingredients.joinToString(", ")}]: ")
        val newAvailability = readAvailabilityOptional("Available [${if (item.isAvailable) "Yes" else "No"}]: ")

        try {
            val success = menuService.updateMenuItem(
                id = item.id,
                name = newName,
                description = newDescription,
                price = newPrice,
                category = newCategory,
                isAvailable = newAvailability,
                preparationTime = newPreparationTime,
                ingredients = newIngredients,
                calories = newCalories
            )

            if (success) {
                println("\n Menu item updated successfully!")
            } else {
                println("\nFailed to update menu item.")
            }
        } catch (e: Exception) {
            println("\nError updating menu item: ${e.message}")
        }
    }

    /**
     * Deletes a menu item.
     */
    private fun deleteMenuItem() = runBlocking {
        println("\n=== Delete Menu Item ===")
        viewMenu()
        val item = findMenuItem("Enter menu item ID to delete: ") ?: return@runBlocking
        
        println("\nAre you sure you want to delete '${item.name}'? (yes/no): ")
        val confirmation = readLine()?.trim()?.lowercase()
        
        if (confirmation == "yes" || confirmation == "y") {
            try {
                val success = menuService.deleteMenuItem(item.id)
                
                if (success) {
                    println("\nMenu item deleted successfully!")
                } else {
                    println("\nFailed to delete menu item.")
                }
            } catch (e: Exception) {
                println("\nError deleting menu item: ${e.message}")
            }
        } else {
            println("\nOperation cancelled.")
        }
    }

    /**
     * Helper function to find a menu item by ID.
     */
    private suspend fun findMenuItem(prompt: String): MenuItem? {
        try {
            while (true) {
                print("\n$prompt")
                val itemId = readLine()?.trim() ?: ""
                
                if (itemId.isBlank()) {
                    println("Operation cancelled.")
                    return null
                }
                
                try {
                    val item = menuService.getMenuItemById(itemId)
                    if (item != null) return item
                    
                    println("Menu item not found. Try again or press Enter to cancel.")
                } catch (e: Exception) {
                    println("Error finding menu item: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
            return null
        }
    }

    /**
     * Displays a menu of categories and allows the user to select one.
     */
    private fun selectCategory(): MenuCategory {
        println("\nSelect a category:")
        val categories = MenuCategory.entries.toTypedArray()
        categories.forEachIndexed { index, category ->
            println("${index + 1}. ${category.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }}")
        }

        while (true) {
            print("Choose a category (1-${categories.size}): ")
            val choice = readIntInRange(1..categories.size)
            return categories[choice - 1]
        }
    }

    private fun selectCategoryOptional(prompt: String): MenuCategory? {
        print("\n$prompt (yes/no to change): ")
        val change = readLine()?.trim()?.equals("yes", ignoreCase = true) ?: false
        return if (change) selectCategory() else null
    }

    /**
     * Helper function to read a non-empty input from the user.
     */
    private fun readNonEmptyInput(prompt: String, errorMessage: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (!input.isNullOrBlank()) return input
            println(errorMessage)
        }
    }

    /**
     * Helper function to read an optional input from the user.
     */
    private fun readOptionalInput(prompt: String): String? {
        print(prompt)
        return readLine()?.trim()?.takeIf { it.isNotBlank() }
    }

    /**
     * Helper function to read an integer within a specified range from user input.
     */
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

    private fun readBigDecimalOptional(prompt: String): BigDecimal? {
        print(prompt)
        val input = readLine()?.trim()
        if (input.isNullOrBlank()) return null
        return try {
            input.toBigDecimal()
        } catch (e: NumberFormatException) {
            println("Invalid number format. Keeping original value.")
            null
        }
    }

    private fun readInt(prompt: String, default: Int): Int {
        print(prompt)
        return readLine()?.trim()?.toIntOrNull() ?: default
    }

    private fun readIntOptional(prompt: String): Int? {
        print(prompt)
        val input = readLine()?.trim()
        if (input.isNullOrBlank()) return null
        return try {
            input.toInt()
        } catch (e: NumberFormatException) {
            println("Invalid number format. Skipping.")
            null
        }
    }

    private fun readIngredientsOptional(prompt: String): List<String>? {
        print(prompt)
        val input = readLine()?.trim()
        if (input.isNullOrBlank()) return null
        return input.split(",").map { it.trim() }.filter { it.isNotBlank() }
    }

    private fun readAvailabilityOptional(prompt: String): Boolean? {
        print(prompt)
        return when (readLine()?.trim()?.lowercase()) {
            "yes", "y" -> true
            "no", "n" -> false
            else -> null
        }
    }
}