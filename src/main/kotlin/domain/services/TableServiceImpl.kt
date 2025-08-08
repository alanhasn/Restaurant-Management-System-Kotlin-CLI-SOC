package domain.services

import domain.models.Table
import domain.models.utils.TableStatus
import java.util.UUID

class TableServiceImpl : TableService {

    // تخزين الجداول في الذاكرة بشكل مؤقت باستخدام map (مفتاح: id)
    private val tables = mutableMapOf<String, Table>()

    override fun addTable(name: String, capacity: Int): Boolean {
        if (name.isBlank() || capacity <= 0) return false

        val newTable = Table(
            id = UUID.randomUUID().toString(),
            tableNumber = name.toIntOrNull() ?: (tables.size + 1), // إذا كان الاسم رقم نستخدمه كرقم الطاولة وإلا رقم تسلسلي
            capacity = capacity,
            status = TableStatus.AVAILABLE
        )

        tables[newTable.id] = newTable
        return true
    }

    override fun updateTable(
        id: String,
        name: String?,
        capacity: Int?,
        isAvailable: Boolean?
    ): Boolean {
        val existingTable = tables[id] ?: return false

        val updatedTable = existingTable.copy(
            tableNumber = name?.toIntOrNull() ?: existingTable.tableNumber,
            capacity = capacity ?: existingTable.capacity,
            status = when (isAvailable) {
                true -> TableStatus.AVAILABLE
                false -> TableStatus.OCCUPIED
                null -> existingTable.status
            }
        )

        tables[id] = updatedTable
        return true
    }

    override fun deleteTable(id: String): Boolean {
        return tables.remove(id) != null
    }

    override fun getTableById(id: String): Table? {
        return tables[id]
    }

    override fun listAllTables(): List<Table> {
        return tables.values.toList()
    }

    override fun markTableAsOccupied(id: String): Boolean {
        val existingTable = tables[id] ?: return false
        if (existingTable.status == TableStatus.OCCUPIED) return false

        tables[id] = existingTable.copy(status = TableStatus.OCCUPIED)
        return true
    }

    override fun markTableAsAvailable(id: String): Boolean {
        val existingTable = tables[id] ?: return false
        if (existingTable.status == TableStatus.AVAILABLE) return false

        tables[id] = existingTable.copy(status = TableStatus.AVAILABLE)
        return true
    }
}
