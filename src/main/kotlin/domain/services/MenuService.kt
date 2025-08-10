package domain.services

import domain.models.MenuItem
import domain.models.utils.MenuCategory
import java.math.BigDecimal

// Interface for menu service operations
interface MenuService {
    suspend fun addMenuItem(
        name: String,
        description: String,
        price: BigDecimal,
        category: MenuCategory,
        preparationTime: Int,
        ingredients: List<String>,
        calories: Int?
    ): Boolean

    suspend fun updateMenuItem(
        id: String,
        name: String?,
        description: String?,
        price: BigDecimal?,
        category: MenuCategory?,
        isAvailable: Boolean?,
        preparationTime: Int?,
        ingredients: List<String>?,
        calories: Int?
    ): Boolean

    suspend fun deleteMenuItem(id: String): Boolean
    suspend fun getMenuItemById(id: String): MenuItem?
    suspend fun listAllMenuItems(): List<MenuItem>
}
