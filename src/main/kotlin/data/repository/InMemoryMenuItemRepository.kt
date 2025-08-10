package data.repository

// Dependencies
import domain.models.MenuItem
import domain.models.utils.MenuCategory
import java.math.BigDecimal
import java.util.UUID // For MenuItem ID generation

// In-Memory MenuItem Repository (Implementation of MenuItemRepository interface)
class InMemoryMenuItemRepository: MenuItemRepository{

    // In-Memory MenuItem Repository
    private val menuItems = mutableMapOf<String , MenuItem>()

    // add menu items
    init {
        menuItems["1"] = MenuItem(
            id = "1",
            name = "Spaghetti",
            description = "A classic Italian pasta dish",
            category = MenuCategory.MAIN_COURSE,
            price = BigDecimal("12.99"),
            preparationTime = 20,
            ingredients = listOf("spaghetti", "tomato sauce", "meatballs"),
            calories = 600
        )
        menuItems["2"] = MenuItem(
            id = "2",
            name = "Pizza",
            description = "A classic Italian pizza",
            category = MenuCategory.BEVERAGE,
            price = BigDecimal("9.99"),
            preparationTime = 15,
            ingredients = listOf("dough", "tomato sauce", "cheese", "pepperoni"),
            calories = 800
        )
        menuItems["3"] = MenuItem(
            id = "3",
            name = "Burger",
            description = "A classic American burger",
            category = MenuCategory.DESSERT,
            price = BigDecimal("7.99"),
            preparationTime = 10,
            ingredients = listOf("bread", "beef patty", "lettuce", "tomato", "mayo"),
            calories = 500
        )
        menuItems["4"] = MenuItem(
            id = "4",
            name = "Fries",
            description = "A classic American fries",
            category = MenuCategory.DESSERT,
            price = BigDecimal("5.99"),
            preparationTime = 10,
            ingredients = listOf("potato", "salt", "pepper"),
            calories = 300
        )
        menuItems["5"] = MenuItem(
            id = "5",
            name = "Soda",
            description = "A classic American soda",
            category = MenuCategory.DESSERT,
            price = BigDecimal("2.99"),
            preparationTime = 10,
            ingredients = listOf("water", "sugar", "carbonated water"),
            calories = 100
        )
    }
    /*
     * Save a MenuItem to the in-memory storage
     * @param menuItem The MenuItem to be saved
     * @return true if the MenuItem was saved successfully
     */
    override fun save(menuItem: MenuItem): Boolean {
        val id = menuItem.id.ifBlank { UUID.randomUUID().toString() }
        val menuItemWithId = menuItem.copy(id=id)
        menuItems[id] = menuItemWithId
        return true
    }

    /*
     * Find a MenuItem by its ID
     * @param id The ID of the MenuItem to find
     * @return The MenuItem with the specified ID, or null if not found
     */
    override fun findById(id: String): MenuItem? {
        return menuItems[id]
    }

    /*
     * Find a MenuItem by its name
     * @param name The name of the MenuItem to find
     * @return The MenuItem with the specified name, or null if not found
     */
    override fun findByName(name: String): MenuItem? {
        return menuItems.values.firstOrNull{ it.name == name }
    }

    /*
     * Find all MenuItems
     * @return A list of all MenuItems
     */
    override fun findAll(): List<MenuItem> {
        return menuItems.values.toList()
    }

    /*
     * Find MenuItems by their category
     * @param category The category of the MenuItems to find
     * @return A list of MenuItems with the specified category
     */
    override fun findByCategory(category: MenuCategory): List<MenuItem> {
        return menuItems.values.filter { it.category == category }
    }

    /*
     * Find all available MenuItems
     * @return A list of all available MenuItems
     */
    override fun findAvailableItems(): List<MenuItem> {
        return menuItems.values.filter { it.isAvailable }
    }

    /*
     * Update a MenuItem in the in-memory storage
     * @param menuItem The MenuItem to be updated
     * @return true if the MenuItem was updated successfully
     */
    override fun update(menuItem: MenuItem): Boolean {
        val id = menuItem.id

        if (!menuItems.containsKey(id)) {
            return false
        }
        menuItems[id] = menuItem
        return true
    }

    /*
     * Delete a MenuItem by its ID
     * @param id The ID of the MenuItem to delete
     * @return True if the MenuItem was deleted, false otherwise
     */
    override fun delete(id: String): Boolean {
        return menuItems.remove(id) != null
    }
}