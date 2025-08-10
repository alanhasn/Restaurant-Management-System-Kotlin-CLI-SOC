package data.repository

import domain.models.Table
import domain.models.utils.TableStatus

// interface for table repository
interface TableRepository {
    fun save(table: Table): Boolean
    fun findById(id: String): Table?
    fun findByTableNumber(tableNumber: Int): Table?
    fun findAll(): List<Table>
    fun findByStatus(status: TableStatus): List<Table>
    fun update(table: Table): Boolean
    fun delete(id: String): Boolean
}
