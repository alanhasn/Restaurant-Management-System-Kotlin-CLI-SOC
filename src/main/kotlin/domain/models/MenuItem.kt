package domain.models

import domain.models.utils.MenuCategory
import java.math.BigDecimal // Import the BigDecimal class for handling decimal values

/*
 * MenuItem class represents a menu item in a restaurant.
 * It contains information about the item, including its name, description, price, category, preparation time, ingredients, and calories.
 */
data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val category: MenuCategory,
    val isAvailable: Boolean = true,
    val preparationTime: Int, // in minutes
    val ingredients: List<String> = emptyList(),
    val calories: Int? = null,
)

