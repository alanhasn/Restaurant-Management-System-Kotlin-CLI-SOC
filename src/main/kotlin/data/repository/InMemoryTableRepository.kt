package data.repository

import domain.models.Table
import domain.models.utils.TableStatus
import java.util.UUID


class InMemoryTableRepository: TableRepository{
    private val tables = mutableMapOf<String , Table>()

    override fun save(table: Table): Table {
        val id = table.id.ifBlank { UUID.randomUUID().toString() }
        val tableWithId = table.copy(id=id)
        tables[id] = tableWithId
        return tableWithId
    }

    override fun findById(id: String): Table? {
        return tables[id]
    }

    override fun findByTableNumber(tableNumber: Int): Table? {
        return tables.values.firstOrNull { it.tableNumber == tableNumber }
    }

    override fun findAll(): List<Table> {
        return tables.values.toList()
    }

    override fun findByStatus(status: TableStatus): List<Table> {
        return tables.values.filter { it.status == status}
    }


    override fun update(table: Table): Result<Table> {
        val id = table.id
        if (!tables.containsKey(id)){
            return Result.failure(IllegalArgumentException("No Table found with id: $id"))
        }
        tables[id] = table
        return Result.success(table)
    }

    override fun delete(id: String): Boolean {
        return tables.remove(id) != null
    }
}