package domain.services

// Dependencies
import data.repository.MenuItemRepository
import domain.models.MenuItem
import domain.models.utils.MenuCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.UUID

// Menu Service Implementation
class MenuServiceImpl(
    private val menuRepository: MenuItemRepository // Inject the MenuItemRepository
) : MenuService {
    /**
     * Adds a new menu item to the menu.
     * @param name - The name of the menu item
     * @param description - The description of the menu item
     * @param price - The price of the menu item
     * @param category - The category of the menu item
     * @param preparationTime - The preparation time of the menu item
     * @param ingredients - The ingredients of the menu item
     * @param calories - The calories of the menu item
     */
    override suspend fun addMenuItem(
        name: String,
        description: String,
        price: BigDecimal,
        category: MenuCategory,
        preparationTime: Int,
        ingredients: List<String>,
        calories: Int?
    ): Boolean = withContext(Dispatchers.IO) {
        if (name.isBlank() || price <= BigDecimal.ZERO) return@withContext false // Validate input

        val newItem = MenuItem(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            price = price,
            category = category,
            preparationTime = preparationTime,
            ingredients = ingredients,
            calories = calories
        )
        delay(1000)
        return@withContext menuRepository.save(newItem)
    }

    /*
     * Updates an existing menu item in the menu.

     * Updates an existing menu item in the menu.
     * @param id - The ID of the menu item to update
     * @param name - The new name of the menu item
     * @param description - The new description of the menu item
     * @param price - The new price of the menu item
     * @param category - The new category of the menu item
     * @param isAvailable - The new availability of the menu item
     * @param preparationTime - The new preparation time of the menu item
     * @param ingredients - The new ingredients of the menu item
     * @param calories - The new calories of the menu item
     */
    override suspend fun updateMenuItem(
        id: String,
        name: String?,
        description: String?,
        price: BigDecimal?,
        category: MenuCategory?,
        isAvailable: Boolean?,
        preparationTime: Int?,
        ingredients: List<String>?,
        calories: Int?
    ): Boolean = withContext(Dispatchers.IO) {
        val existingItem = menuRepository.findById(id) ?: return@withContext false // Validate input

        // Update the menu item
        val updatedItem = existingItem.copy(
            name = name?.takeIf { it.isNotBlank() } ?: existingItem.name, // Use the new name if provided, otherwise use the existing name
            description = description ?: existingItem.description,
            price = price?.takeIf { it > BigDecimal.ZERO } ?: existingItem.price,
            category = category ?: existingItem.category,
            isAvailable = isAvailable ?: existingItem.isAvailable,
            preparationTime = preparationTime ?: existingItem.preparationTime,
            ingredients = ingredients ?: existingItem.ingredients,
            calories = calories ?: existingItem.calories
        )
        delay(1000)
        return@withContext menuRepository.save(updatedItem)
    }

    /*
     * Deletes a menu item from the menu.
     * @param id - The ID of the menu item to delete
     */
    override suspend fun deleteMenuItem(id: String): Boolean = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext menuRepository.delete(id)
    }

    /*
     * Retrieves a menu item by its ID.
     * @param id - The ID of the menu item to retrieve
     * @return The menu item object if found, null otherwise
     */
    override suspend fun getMenuItemById(id: String): MenuItem? {
        return menuRepository.findById(id)
    }

    /*
     * Lists all menu items in the menu.
     * @return A list of all menu items
     */
    override suspend fun listAllMenuItems(): List<MenuItem> {
        delay(1000)
        return menuRepository.findAll()
    }
}
