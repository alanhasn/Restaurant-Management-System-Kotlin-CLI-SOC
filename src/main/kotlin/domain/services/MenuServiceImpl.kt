package domain.services

import data.repository.MenuItemRepository
import domain.models.MenuItem
import domain.models.utils.MenuCategory
import java.math.BigDecimal
import java.util.UUID

class MenuServiceImpl(
    private val menuItemRepository: MenuItemRepository
) : MenuService {

    override fun addMenuItem(
        name: String,
        price: Double,
        category: MenuCategory,
        description: String,
        preparationTime: Int,
        calories: Int?
    ): Boolean {
        if (name.isBlank() || price <= 0.0) return false

        val newItem = MenuItem(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            price = BigDecimal.valueOf(price),
            category = category,  // مثلاً يمكنك تعديل هذه القيمة أو إضافة معلمة category
            preparationTime = preparationTime
        )
        return menuItemRepository.save(newItem)
    }

    override fun updateMenuItem(id: String, name: String?, price: Double?): Boolean {
        val existingItem = menuItemRepository.findById(id) ?: return false

        val updatedItem = existingItem.copy(
            name = name?.takeIf { it.isNotBlank() } ?: existingItem.name,
            price = price?.takeIf { it > 0 }?.let { BigDecimal.valueOf(it) } ?: existingItem.price
        )
        return menuItemRepository.update(updatedItem)
    }

    override fun deleteMenuItem(id: String): Boolean {
        return menuItemRepository.delete(id)
    }

    override fun getMenuItemById(id: String): MenuItem? {
        return menuItemRepository.findById(id)
    }

    override fun listAllMenuItems(): List<MenuItem> {
        return menuItemRepository.findAll()
    }
}
