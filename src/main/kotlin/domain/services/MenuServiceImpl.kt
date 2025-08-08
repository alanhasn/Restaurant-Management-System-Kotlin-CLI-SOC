package domain.services

import data.repository.MenuItemRepository
import domain.models.MenuItem
import domain.models.utils.MenuCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.UUID

class MenuServiceImpl(
    private val menuItemRepository: MenuItemRepository
) : MenuService {

    override suspend fun addMenuItem(
        name: String,
        price: Double,
        category: MenuCategory,
        description: String,
        preparationTime: Int,
        calories: Int?
    ): Boolean = withContext(Dispatchers.IO) {
        if (name.isBlank() || price <= 0.0) return@withContext false

        val newItem = MenuItem(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            price = BigDecimal.valueOf(price),
            category = category,  // مثلاً يمكنك تعديل هذه القيمة أو إضافة معلمة category
            preparationTime = preparationTime
        )
        delay(1000)
        return@withContext menuItemRepository.save(newItem)
    }

    override suspend fun updateMenuItem(id: String, name: String?, price: Double?): Boolean=withContext(Dispatchers.IO){
        val existingItem = menuItemRepository.findById(id) ?: return@withContext false

        val updatedItem = existingItem.copy(
            name = name?.takeIf { it.isNotBlank() } ?: existingItem.name,
            price = price?.takeIf { it > 0 }?.let { BigDecimal.valueOf(it) } ?: existingItem.price
        )
        delay(1000)
        return@withContext menuItemRepository.update(updatedItem)
    }

    override suspend fun deleteMenuItem(id: String): Boolean =withContext(Dispatchers.IO){
        delay(1000)
        return@withContext menuItemRepository.delete(id)
    }

    override suspend fun getMenuItemById(id: String): MenuItem? {
        return menuItemRepository.findById(id)
    }

    override suspend fun listAllMenuItems(): List<MenuItem> {
        delay(1000)
        return menuItemRepository.findAll()
    }
}
