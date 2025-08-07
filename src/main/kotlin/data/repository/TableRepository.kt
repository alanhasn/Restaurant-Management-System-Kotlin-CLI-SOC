package data.repository

import domain.models.Table
import domain.models.utils.TableStatus

interface TableRepository {
    fun save(table: Table): Table
    fun findById(id: String): Table?
    fun findByTableNumber(tableNumber: Int): Table?
    fun findAll(): List<Table>
    fun findByStatus(status: TableStatus): List<Table>
    fun update(table: Table): Result<Table>
    fun delete(id: String): Boolean
}
