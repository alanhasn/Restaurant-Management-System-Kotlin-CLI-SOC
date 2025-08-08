package domain.services

import domain.models.MenuItem
import domain.models.utils.MenuCategory
import jdk.jfr.Description


interface MenuService {
    fun addMenuItem(
        name: String,
        price: Double,
        category: MenuCategory,
        description: String,
        preparationTime: Int,
        calories: Int?
    ): Boolean
    fun updateMenuItem(id: String, name: String?, price: Double?): Boolean
    fun deleteMenuItem(id: String): Boolean
    fun getMenuItemById(id: String): MenuItem?
    fun listAllMenuItems(): List<MenuItem>
}
