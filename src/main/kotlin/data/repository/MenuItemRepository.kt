package data.repository

import domain.models.utils.MenuCategory
import domain.models.MenuItem

// interface for MenuItemRepository
interface MenuItemRepository {
    fun save(menuItem: MenuItem): Boolean
    fun findById(id: String): MenuItem?
    fun findByName(name: String): MenuItem?
    fun findAll(): List<MenuItem>
    fun findByCategory(category: MenuCategory): List<MenuItem>
    fun findAvailableItems(): List<MenuItem>
    fun update(menuItem: MenuItem): Boolean
    fun delete(id: String): Boolean
}
