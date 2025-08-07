package data.repository

import domain.models.MenuItem
import domain.models.utils.MenuCategory
import java.util.UUID


class InMemoryMenuItemRepository: MenuItemRepository{
    private val menuItems = mutableMapOf<String , MenuItem>()

    override fun save(menuItem: MenuItem): MenuItem {
        val id = menuItem.id.ifBlank { UUID.randomUUID().toString() }
        val menuItemWithId = menuItem.copy(id=id)
        menuItems[id] = menuItemWithId
        return menuItemWithId
    }

    override fun findById(id: String): MenuItem? {
        return menuItems[id]
    }

    override fun findByName(name: String): MenuItem? {
        return menuItems.values.firstOrNull{ it.name == name }
    }

    override fun findAll(): List<MenuItem> {
        return menuItems.values.toList()
    }

    override fun findByCategory(category: MenuCategory): List<MenuItem> {
        return menuItems.values.filter { it.category == category }
    }

    override fun findAvailableItems(): List<MenuItem> {
        return menuItems.values.filter { it.isAvailable }
    }

    override fun update(menuItem: MenuItem): Result<MenuItem> {
        val id = menuItem.id

        if (!menuItems.containsKey(id)) {
            return Result.failure(IllegalArgumentException("No menu item found with id: $id"))
        }
        menuItems[id] = menuItem
        return Result.success(menuItem)
    }

    override fun delete(id: String): Boolean {
        return menuItems.remove(id) != null
    }
}