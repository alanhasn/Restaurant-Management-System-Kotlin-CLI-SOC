package domain.models

import domain.models.utils.TableStatus


data class Table(
    val id: String,
    val tableNumber: Int,
    val capacity: Int,
    val status: TableStatus = TableStatus.AVAILABLE,
    val location: String? = null,
    val description: String? = null
)
