package domain.services

import domain.models.MenuItem


interface MenuService {
    fun addMenuItem(name: String, price: Double): Boolean
    fun updateMenuItem(id: String, name: String?, price: Double?): Boolean
    fun deleteMenuItem(id: String): Boolean
    fun getMenuItemById(id: String): MenuItem?
    fun listAllMenuItems(): List<MenuItem>
}
