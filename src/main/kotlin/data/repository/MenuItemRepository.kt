package data.repository

import domain.models.utils.MenuCategory
import domain.models.MenuItem

interface MenuItemRepository {
    fun save(menuItem: MenuItem): MenuItem
    fun findById(id: String): MenuItem?
    fun findByName(name: String): MenuItem?
    fun findAll(): List<MenuItem>
    fun findByCategory(category: MenuCategory): List<MenuItem>
    fun findAvailableItems(): List<MenuItem>
    fun update(menuItem: MenuItem): Result<MenuItem>
    fun delete(id: String): Boolean
}
