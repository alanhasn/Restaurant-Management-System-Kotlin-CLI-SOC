package domain.models

import domain.models.utils.MenuCategory
import java.math.BigDecimal

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

