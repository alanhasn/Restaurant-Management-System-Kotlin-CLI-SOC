package domain.services

import domain.models.MenuItem
import domain.models.utils.MenuCategory
import jdk.jfr.Description


interface MenuService {
    suspend fun addMenuItem(
        name: String,
        price: Double,
        category: MenuCategory,
        description: String,
        preparationTime: Int,
        calories: Int?
    ): Boolean
    suspend fun updateMenuItem(id: String, name: String?, price: Double?): Boolean
    suspend fun deleteMenuItem(id: String): Boolean
    suspend fun getMenuItemById(id: String): MenuItem?
    suspend fun listAllMenuItems(): List<MenuItem>
}
